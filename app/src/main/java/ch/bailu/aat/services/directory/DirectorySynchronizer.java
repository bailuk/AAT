package ch.bailu.aat.services.directory;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.GpxListAttributes;
import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat.map.mapsforge.MapsForgePreview;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundTask;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;

public class DirectorySynchronizer  implements Closeable {

    private GpxObject pendingHandle=null;
    private MapsForgePreview pendingPreviewGenerator=null;

    private FilesInDirectory filesToAdd=null;
    private final ArrayList<String> filesToRemove = new ArrayList<>();

    private GpxDatabase database;

    private long dbAccessTime;

    private final Foc directory;
    private final ServiceContext scontext;

    private boolean canContinue=true;
    private State state;

    public DirectorySynchronizer(ServiceContext cs, Foc d) {
        scontext=cs;
        directory=d;

        if (scontext.lock()) {
            setState(new StateInit());
        }
    }



    private final BroadcastReceiver onFileChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            state.ping();
        }
    };



    /////////////////////////////////////////////////////////////////////////////////////////////
    private abstract class State {
        public State() {}
        public abstract void start();
        public abstract void ping();
    }


    private void setState(State s) {
        if (canContinue) { 
            state = s;
            state.start();
        } else {
            terminate();
        }

    }

    private void terminate(Exception e) {
        state = new StateTerminate(e);
        state.start();
    }

    private void terminate() {
        state = new StateTerminate();
        state.start();
    }



    /////////////////////////////////////////////////////////////////////////////////////////////    
    private class StateInit extends State {
        /**
         *  TODO: move db open into background
         */
        @Override
        public void start() {

            AppBroadcaster.register(scontext.getContext(),
                    onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
            try {
                database = openDatabase();
                setState(new StatePrepareSync());
            } catch (Exception e) {
                terminate(e);
            }
        }




        private GpxDatabase openDatabase()  throws Exception {
            final String dbPath = SummaryConfig.getWriteableDBPath(scontext.getContext(), directory);
            final String query[] = {GpxDbConstants.KEY_FILENAME};

            dbAccessTime = new File(dbPath).lastModified();
            return new GpxDatabase(scontext, dbPath, query);
        }

        @Override
        public void ping() {}
    }




    /////////////////////////////////////////////////////////////////////////////////////////////    
    private class StatePrepareSync extends State {
        private Exception exception=null;

        private BackgroundTask backgroundTask = new BackgroundTask() {

            @Override
            public long bgOnProcess(ServiceContext sc) {
                try {

                    filesToAdd = new FilesInDirectory(directory);
                    compareFileSystemWithDatabase();
                    removeFilesFromDatabase();

                } catch (IOException e) {
                    exception = e;

                } finally {
                    backgroundTask = null;

                    AppBroadcaster.broadcast(sc.getContext(),
                            AppBroadcaster.FILE_CHANGED_INCACHE, directory);
                }

                return 100;

            }



        };

        @Override
        public void start() {
            AppBroadcaster.broadcast(scontext.getContext(), AppBroadcaster.DBSYNC_START);

            scontext.getBackgroundService().process(backgroundTask);
        }


        @Override
        public void ping() {
            if (backgroundTask == null) {
                if (exception == null) {
                    setState(new StateLoadNextGpx());
                } else {
                    terminate(exception);
                }
            }
        }



        private void removeFilesFromDatabase() throws IOException {
            if (canContinue && filesToRemove.size()>0) {
                for (int i=0; canContinue && i<filesToRemove.size(); i++) {

                    removeFileFromDatabase(filesToRemove.get(i));
                }
                AppBroadcaster.broadcast(scontext.getContext(), AppBroadcaster.DB_SYNC_CHANGED);
            }
        }


        private void removeFileFromDatabase(String name) {
            final Foc file = directory.child(name);

            SummaryConfig.getWriteablePreviewFile(scontext.getContext(), file).rm();
            database.deleteEntry(file);
        }



        private void compareFileSystemWithDatabase() {
            final Cursor cursor = database.query(null);

            for (boolean r=cursor.moveToFirst(); canContinue && r; r=cursor.moveToNext()) {
                final String name = getFileName(cursor);
                final Foc file = filesToAdd.findItem(name);

                if (file == null) {
                    filesToRemove.add(name);

                } else if (isFileInSync(file)) {
                    filesToAdd.pollItem(file);

                } else {
                    filesToRemove.add(name);
                }
            }
            cursor.close();
        }

        private String getFileName(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(GpxDbConstants.KEY_FILENAME));
        }


        private boolean isFileInSync(Foc file) {
            if (file.lastModified() < System.currentTimeMillis()) {
                return file.lastModified() < dbAccessTime; 
            } 
            return true;
        }

    }




    /////////////////////////////////////////////////////////////////////////////////////////////
    private class StateLoadNextGpx extends State {

        public void start() {

            Foc file = filesToAdd.pollItem();


            if (file==null) {
                terminate();


            } else {
                ObjectHandle h = scontext.getCacheService().getObject(
                        file.getPath(), new GpxObjectStatic.Factory());
                if (h instanceof GpxObject) {

                    setPendingGpxHandle((GpxObject)h);
                    state.ping();

                } else {
                    h.free();
                    state.start();
                }
            }
        }

        @Override
        public void ping() {
            if (canContinue == false) {
                terminate();

            } else if (pendingHandle.isReadyAndLoaded()) {
                try {
                    addGpxSummaryToDatabase(pendingHandle.getID(), pendingHandle.getGpxList());
                    setState(new StateLoadPreview());
                } catch (Exception e) {
                    terminate(e);
                }

            }  else if (pendingHandle.hasException()) {
                state.start();
                //setState(new StateLoadNextGpx());

            }
        }


        private void addGpxSummaryToDatabase(String id, GpxList list) {
            final Foc file = FocAndroid.factory(scontext.getContext(), id);

            ContentValues content = createContentValues(file.getName(), list.getDelta());
            database.insert(content);
        }

        private ContentValues createContentValues(String filename,
                GpxBigDeltaInterface summary) {

            BoundingBoxE6 bounding = summary.getBoundingBox();

            ContentValues content = new ContentValues();
            content.put(GpxDbConstants.KEY_FILENAME,   filename);
            content.put(GpxDbConstants.KEY_AVG_SPEED,  summary.getSpeed());

            content.put(GpxDbConstants.KEY_MAX_SPEED,  summary.getAttributes().get(
                    GpxListAttributes.INDEX_MAX_SPEED));

            content.put(GpxDbConstants.KEY_DISTANCE,   summary.getDistance());
            content.put(GpxDbConstants.KEY_START_TIME, summary.getStartTime());
            content.put(GpxDbConstants.KEY_TOTAL_TIME, summary.getTimeDelta());
            content.put(GpxDbConstants.KEY_END_TIME,   summary.getEndTime());        
            content.put(GpxDbConstants.KEY_PAUSE,      summary.getPause());
            content.put(GpxDbConstants.KEY_TYPE_ID,    summary.getType().toInteger());
            content.put(GpxDbConstants.KEY_EAST_BOUNDING, bounding.getLonEastE6());
            content.put(GpxDbConstants.KEY_WEST_BOUNDING, bounding.getLonWestE6());
            content.put(GpxDbConstants.KEY_NORTH_BOUNDING, bounding.getLatNorthE6());
            content.put(GpxDbConstants.KEY_SOUTH_BOUNDING, bounding.getLatSouthE6());
            return content;
        }

    }


    private void setPendingGpxHandle(GpxObject h) {
        if (pendingHandle != null) {
            pendingHandle.free();
        }
        pendingHandle = h;
    }



    /////////////////////////////////////////////////////////////////////////////////////////////    
    private class StateLoadPreview extends State {

        public void start() {

            Foc gpxFile = FocAndroid.factory(scontext.getContext(), pendingHandle.getID());

            Foc previewImageFile = SummaryConfig.getWriteablePreviewFile(
                    scontext.getContext(),
                    gpxFile);

            GpxInformation info =
                    new GpxFileWrapper(gpxFile, pendingHandle.getGpxList());


            try {
                MapsForgePreview p = new MapsForgePreview(scontext, info, previewImageFile);

                setPendingPreviewGenerator(p);
                state.ping();

            } catch (Exception e) {
                AppLog.w(this, e);

                AppBroadcaster.broadcast(scontext.getContext(), AppBroadcaster.DB_SYNC_CHANGED);
                setState(new StateLoadNextGpx());
            }



        }

        @Override
        public void ping() {
            if (canContinue==false) {
                terminate();
            } else if (pendingPreviewGenerator.isReady()) {

                pendingPreviewGenerator.generateBitmapFile();

                AppBroadcaster.broadcast(scontext.getContext(), AppBroadcaster.DB_SYNC_CHANGED);
                setState(new StateLoadNextGpx());
            }
        }
    }


    private void setPendingPreviewGenerator(MapsForgePreview g) {
        if (pendingPreviewGenerator != null) {
            pendingPreviewGenerator.onDestroy();
        }
        pendingPreviewGenerator=g;
    }



    /////////////////////////////////////////////////////////////////////////////////////////////
    private class StateTerminate extends State {

        public StateTerminate(Exception e) {
            e.printStackTrace();
            AppLog.e(scontext.getContext(), e);
        }

        public StateTerminate() {}

        @Override
        public void ping() {}

        @Override
        public void start() {
            scontext.getContext().unregisterReceiver(onFileChanged);

            if (database != null) {
                database.close();
            }

            setPendingGpxHandle(null);
            setPendingPreviewGenerator(null);

            AppBroadcaster.broadcast(scontext.getContext(), AppBroadcaster.DBSYNC_DONE);


            scontext.free();
        }
    }


    @Override
    public synchronized void close() {
        canContinue=false;
        state.ping();
    }
}