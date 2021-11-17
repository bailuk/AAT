package ch.bailu.aat_lib.service.directory;

import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.aat_lib.gpx.GpxFileWrapper;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed;
import ch.bailu.aat_lib.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.background.BackgroundTask;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjGpx;
import ch.bailu.aat_lib.util.sql.DbFactory;
import ch.bailu.aat_lib.util.sql.ResultSet;
import ch.bailu.foc.Foc;


public final class DirectorySynchronizer  implements Closeable {

    private ObjGpx pendingHandle=null;
    private MapPreviewInterface pendingPreviewGenerator=null;

    private FilesInDirectory filesToAdd=null;
    private final ArrayList<String> filesToRemove = new ArrayList<>();

    private GpxDatabase database;

    private long dbAccessTime;

    private final Foc directory;
    private final ServicesInterface scontext;

    private boolean canContinue=true;
    private State state;

    private final Broadcaster broadcaster;
    private final FocFactory focFactory;
    private final SummaryConfig summaryConfig;
    private final DbFactory dbFactory;
    private final MapPreviewFactory mapPreviewFactory;


    public DirectorySynchronizer(ServicesInterface cs, Broadcaster broadcaster, FocFactory focFactory, SummaryConfig summaryConfig, DbFactory dbFactory, MapPreviewFactory mapPreviewFactory, Foc d) {
        scontext=cs;
        directory=d;
        this.mapPreviewFactory = mapPreviewFactory;
        this.dbFactory = dbFactory;
        this.broadcaster = broadcaster;
        this.focFactory = focFactory;
        this.summaryConfig = summaryConfig;

        if (scontext.lock()) {
            setState(new StateInit());
        }
    }



    private final BroadcastReceiver onFileChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Object... objs) {
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
    private final class StateInit extends State {
        /**
         *  TODO: move db open into background
         */
        @Override
        public void start() {
            broadcaster.register(onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
            try {
                database = openDatabase();
                setState(new StatePrepareSync());
            } catch (Exception e) {
                terminate(e);
            }
        }

        private GpxDatabase openDatabase() {
            final String dbPath = summaryConfig.getDBPath(directory);
            final String[] query = {GpxDbConstants.KEY_FILENAME};

            dbAccessTime = new File(dbPath).lastModified();
            return new GpxDatabase(dbFactory.createDataBase(), dbPath, query);
        }

        @Override
        public void ping() {}
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    private final class StatePrepareSync extends State {
        private Exception exception=null;

        private BackgroundTask backgroundTask = new BackgroundTask() {

            @Override
            public long bgOnProcess(ServicesInterface sc) {
                try {
                    filesToAdd = new FilesInDirectory(directory);
                    compareFileSystemWithDatabase();
                    removeFilesFromDatabase();

                } catch (Exception e) {
                    exception = e;

                } finally {
                    backgroundTask = null;

                    broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, directory);
                }

                return 100;

            }
        };


        @Override
        public void start() {
            broadcaster.broadcast(AppBroadcaster.DBSYNC_START);

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



        private void removeFilesFromDatabase() {
            if (canContinue && filesToRemove.size()>0) {
                for (int i=0; canContinue && i<filesToRemove.size(); i++) {

                    removeFileFromDatabase(filesToRemove.get(i));
                }
                broadcaster.broadcast(AppBroadcaster.DB_SYNC_CHANGED);
            }
        }


        private void removeFileFromDatabase(String name) {
            final Foc file = directory.child(name);

            summaryConfig.getPreviewFile(file).rm();
            database.deleteEntry(file);
        }



        private void compareFileSystemWithDatabase() {
            final ResultSet resultSet = database.query(null);

            for (boolean r=resultSet.moveToFirst(); canContinue && r; r=resultSet.moveToNext()) {
                final String name = getFileName(resultSet);
                final Foc file = filesToAdd.findItem(name);

                if (file == null) {
                    filesToRemove.add(name);

                } else if (isFileInSync(file)) {
                    filesToAdd.pollItem(file);

                } else {
                    filesToRemove.add(name);
                }
            }
            resultSet.close();
        }

        private String getFileName(ResultSet resultSet) {
            int index = resultSet.getColumnIndex(GpxDbConstants.KEY_FILENAME);
            if (index > -1) {
                return resultSet.getString(index);
            }
            return "";
        }


        private boolean isFileInSync(Foc file) {
            if (file.lastModified() < System.currentTimeMillis()) {
                return file.lastModified() < dbAccessTime;
            }
            return true;
        }

    }




    /////////////////////////////////////////////////////////////////////////////////////////////
    private final class StateLoadNextGpx extends State {

        public void start() {

            Foc file = filesToAdd.pollItem();


            if (file==null) {
                terminate();


            } else {
                Obj h = scontext.getCacheService().getObject(
                        file.getPath(), new ObjGpxStatic.Factory());
                if (h instanceof ObjGpx) {

                    setPendingGpxHandle((ObjGpx)h);
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
            final Foc file = focFactory.toFoc(id);

            ArrayList<String> keys = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            createContentValues(file.getName(), list.getDelta(), keys, values);
            database.insert((String[]) keys.toArray(), (String[]) values.toArray());
        }

        private void createContentValues(String filename,
                GpxBigDeltaInterface summary, ArrayList<String> keys, ArrayList<String> values) {

            BoundingBoxE6 bounding = summary.getBoundingBox();


            keys.add(GpxDbConstants.KEY_FILENAME);   values.add(filename);
            keys.add(GpxDbConstants.KEY_AVG_SPEED);  values.add(String.valueOf(summary.getSpeed()));

            keys.add(GpxDbConstants.KEY_MAX_SPEED);  values.add(summary.getAttributes().get(
                    MaxSpeed.INDEX_MAX_SPEED));

            keys.add(GpxDbConstants.KEY_DISTANCE);   values.add(String.valueOf(summary.getDistance()));
            keys.add(GpxDbConstants.KEY_START_TIME); values.add(String.valueOf(summary.getStartTime()));
            keys.add(GpxDbConstants.KEY_TOTAL_TIME); values.add(String.valueOf(summary.getTimeDelta()));
            keys.add(GpxDbConstants.KEY_END_TIME);   values.add(String.valueOf(summary.getEndTime()));
            keys.add(GpxDbConstants.KEY_PAUSE);      values.add(String.valueOf(summary.getPause()));
            keys.add(GpxDbConstants.KEY_TYPE_ID);    values.add(String.valueOf(summary.getType().toInteger()));
            keys.add(GpxDbConstants.KEY_EAST_BOUNDING); values.add(String.valueOf(bounding.getLonEastE6()));
            keys.add(GpxDbConstants.KEY_WEST_BOUNDING); values.add(String.valueOf(bounding.getLonWestE6()));
            keys.add(GpxDbConstants.KEY_NORTH_BOUNDING); values.add(String.valueOf(bounding.getLatNorthE6()));
            keys.add(GpxDbConstants.KEY_SOUTH_BOUNDING); values.add(String.valueOf(bounding.getLatSouthE6()));
        }

    }


    private void setPendingGpxHandle(ObjGpx h) {
        if (pendingHandle != null) {
            pendingHandle.free();
        }
        pendingHandle = h;
    }



    /////////////////////////////////////////////////////////////////////////////////////////////
    private final class StateLoadPreview extends State {

        public void start() {

            Foc gpxFile = focFactory.toFoc(pendingHandle.getID());

            Foc previewImageFile = summaryConfig.getPreviewFile(gpxFile);

            GpxInformation info =
                    new GpxFileWrapper(gpxFile, pendingHandle.getGpxList());


            try {
                MapPreviewInterface p = mapPreviewFactory.createMapPreview(info, previewImageFile);

                setPendingPreviewGenerator(p);
                state.ping();

            } catch (Exception e) {
                AppLog.w(this, e);

                broadcaster.broadcast(AppBroadcaster.DB_SYNC_CHANGED);
                setState(new StateLoadNextGpx());
            }



        }

        @Override
        public void ping() {
            if (canContinue==false) {
                terminate();
            } else if (pendingPreviewGenerator.isReady()) {

                pendingPreviewGenerator.generateBitmapFile();

                broadcaster.broadcast(AppBroadcaster.DB_SYNC_CHANGED);
                setState(new StateLoadNextGpx());
            }
        }
    }


    private void setPendingPreviewGenerator(MapPreviewInterface g) {
        if (pendingPreviewGenerator != null) {
            pendingPreviewGenerator.onDestroy();
        }
        pendingPreviewGenerator=g;
    }



    /////////////////////////////////////////////////////////////////////////////////////////////
    private final class StateTerminate extends State {

        public StateTerminate(Exception e) {
            e.printStackTrace();
            AppLog.e(e);
        }

        public StateTerminate() {}

        @Override
        public void ping() {}

        @Override
        public void start() {
            broadcaster.unregister(onFileChanged);

            if (database != null) {
                database.close();
            }

            setPendingGpxHandle(null);
            setPendingPreviewGenerator(null);

            broadcaster.broadcast(AppBroadcaster.DBSYNC_DONE);


            scontext.free();
        }
    }


    @Override
    public synchronized void close() {
        canContinue=false;
        state.ping();
    }
}
