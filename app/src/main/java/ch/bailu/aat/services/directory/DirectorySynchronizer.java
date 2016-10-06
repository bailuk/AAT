package ch.bailu.aat.services.directory;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.osmdroid.util.BoundingBoxE6;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.views.map.OsmPreviewGenerator;

public class DirectorySynchronizer  implements Closeable {

    private GpxObject pendingHandle=null;
    private OsmPreviewGenerator pendingPreviewGenerator=null;

    private FilesOnDisk filesToAdd=null;
    private final ArrayList<String> filesToRemove = new ArrayList<>();

    private SQLiteDatabase database;


    private long dbAccessTime;

    private final File directory;
    private final ServiceContext scontext;

    private boolean canContinue=true;
    private State state;

    public DirectorySynchronizer(ServiceContext cs, File d) {
        scontext=cs;
        directory=d;

        setState(new StateInit());
    }



    private final BroadcastReceiver onFileChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            state.ping();
        }
    };



    /////////////////////////////////////////////////////////////////////////////////////////////
    private abstract class State {
        public State() {
            //AppLog.d(this, "start");
        }
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
            AppBroadcaster.register(scontext.getContext(), onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
            try {
                database = openDatabase();
                setState(new StatePrepareSync());
            } catch (Exception e) {
                terminate(e);
            }
        }




        private SQLiteDatabase openDatabase()  throws Exception {
            SQLiteDatabase database=null;

            File file =  AppDirectory.getCacheDb(directory);

            dbAccessTime = file.lastModified();
            database = new GpxDbOpenHelper(scontext.getContext(), file).getReadableDatabase();

            return database;
        }

        @Override
        public void ping() {}
    }



    /*    
    /////////////////////////////////////////////////////////////////////////////////////////////
    private class StateIdle extends StateMachine {
        @Override
        public void start() {
            AppBroadcaster.broadcast(context, AppBroadcaster.DBSYNC_DONE);
        }

        @Override
        public void ping() {
            if (doSync == true) {
                doSync = false;
                setState(new StatePrepareSync());
            }
        }
    }


    public void synchronize() {
        doSync=true;
        state.ping();
    }

     */  

    /////////////////////////////////////////////////////////////////////////////////////////////    
    private class StatePrepareSync extends State {
        /**
         * TODO: move into background
         */
        public Exception exception=null;

        private ProcessHandle bgProcess = new ProcessHandle() {


            @Override
            public long bgOnProcess() {
                try {
                    filesToAdd = new FilesOnDisk(directory);
                    compareFileSystemWithDatabase();
                    removeFilesFromDatabase();
                } catch (IOException e) {
                    exception = e;
                }

                bgProcess = null;
                return 100;

            }

            @Override
            public void broadcast(Context context) {
                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, directory.toString());
            }

        };

        @Override
        public void start() {
            AppBroadcaster.broadcast(scontext.getContext(), AppBroadcaster.DBSYNC_START);        

            scontext.getBackgroundService().process(bgProcess);
        }


        @Override
        public void ping() {
            if (bgProcess==null) {
                if (exception==null) {
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


        private void removeFileFromDatabase(String filePath) throws IOException {
            AppDirectory.getPreviewFile(new File(filePath)).delete();

            database.delete(GpxDbConstants.DB_TABLE, GpxDbConstants.KEY_PATHNAME + "=\'" + filePath + "\'", null);
        }



        private void compareFileSystemWithDatabase() throws IOException {
            Cursor cursor = openCursor(database);

            for (boolean r=cursor.moveToFirst(); canContinue && r; r=cursor.moveToNext()) {
                String path = getTrackFilePath(cursor);
                File file = filesToAdd.findItem(path);

                if (file == null) {
                    filesToRemove.add(path);

                } else if (isFileInSync(file)) {
                    filesToAdd.popItem(file);

                } else {
                    filesToRemove.add(path);
                }
            }
            cursor.close();
        }

        private String getTrackFilePath(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(GpxDbConstants.KEY_PATHNAME));
        }

        private Cursor openCursor(SQLiteDatabase database) {
            return database.query(
                    GpxDbConstants.DB_TABLE, 
                    new String[] { GpxDbConstants.KEY_PATHNAME }, 
                    null, 
                    null, 
                    null, 
                    null, 
                    null);
        }


        private boolean isFileInSync(File file) {
            if (file.lastModified() < System.currentTimeMillis()) {
                return file.lastModified() < dbAccessTime; 
            } 
            return true;
        }

    }




    /////////////////////////////////////////////////////////////////////////////////////////////
    private class StateLoadNextGpx extends State {

        public void start() {

            File file = filesToAdd.popItem();
            if (file==null) {
                terminate();


            } else {
                ObjectHandle h = scontext.getCacheService().getObject(file.getAbsolutePath(), new GpxObjectStatic.Factory());
                if (GpxObject.class.isInstance(h)) {

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
            } else if (pendingHandle.isReady()) {
                try {
                    addGpxSummaryToDatabase(pendingHandle.toString(),pendingHandle.getGpxList());
                    setState(new StateLoadPreview());
                } catch (IOException e) {
                    terminate(e);
                }
            } 
        }


        private void addGpxSummaryToDatabase(String id, GpxList list) throws IOException {
            final File file = new File(id);

            ContentValues content = createContentValues(file.getPath(), file.getName(), list.getDelta());
            database.insert(GpxDbConstants.DB_TABLE, null, content);
        }

        private ContentValues createContentValues(String pathname, String filename, 
                GpxBigDeltaInterface summary) {

            BoundingBoxE6 box = summary.getBoundingBox().toBoundingBoxE6();

            ContentValues content = new ContentValues();
            content.put(GpxDbConstants.KEY_PATHNAME,   pathname);
            content.put(GpxDbConstants.KEY_FILENAME,   filename);
            content.put(GpxDbConstants.KEY_AVG_SPEED,  summary.getSpeed());
            content.put(GpxDbConstants.KEY_MAX_SPEED,  summary.getMaximumSpeed());
            content.put(GpxDbConstants.KEY_DISTANCE,   summary.getDistance());
            content.put(GpxDbConstants.KEY_START_TIME, summary.getStartTime());
            content.put(GpxDbConstants.KEY_TOTAL_TIME, summary.getTimeDelta());
            content.put(GpxDbConstants.KEY_END_TIME,   summary.getEndTime());        
            content.put(GpxDbConstants.KEY_PAUSE,      summary.getPause());
            content.put(GpxDbConstants.KEY_TYPE_ID,    summary.getType());
            content.put(GpxDbConstants.KEY_EAST_BOUNDING, box.getLonEastE6());
            content.put(GpxDbConstants.KEY_WEST_BOUNDING, box.getLonWestE6());
            content.put(GpxDbConstants.KEY_NORTH_BOUNDING, box.getLatNorthE6());
            content.put(GpxDbConstants.KEY_SOUTH_BOUNDING, box.getLatSouthE6());
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
            File previewImageFile;
            previewImageFile = AppDirectory.getPreviewFile(new File(pendingHandle.toString()));
            setPendingPreviewGenerator(
                    new OsmPreviewGenerator(
                            scontext, 
                            pendingHandle.getGpxList(), 
                            previewImageFile));
            state.ping();


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


    private void setPendingPreviewGenerator(OsmPreviewGenerator g) {
        if (pendingPreviewGenerator != null) {
            pendingPreviewGenerator.close();
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
        }
    }


    @Override
    public synchronized void close() {
        canContinue=false;
        state.ping();
    }
}