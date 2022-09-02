package ch.bailu.aat_lib.service.directory;

import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.gpx.GpxFileWrapper;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed;
import ch.bailu.aat_lib.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.service.background.BackgroundTask;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjGpx;
import ch.bailu.aat_lib.service.cache.ObjGpxStatic;
import ch.bailu.aat_lib.service.directory.database.GpxDatabase;
import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration;
import ch.bailu.aat_lib.util.Objects;
import ch.bailu.aat_lib.util.sql.DbResultSet;
import ch.bailu.foc.Foc;

public final class DirectorySynchronizer  implements Closeable {

    private ObjGpx pendingHandle=null;
    private MapPreviewInterface pendingPreviewGenerator=null;

    private FilesInDirectory filesToAdd=null;
    private final ArrayList<String> filesToRemove = new ArrayList<>();

    private GpxDatabase database;

    private long dbAccessTime;

    private final Foc directory;
    private final AppContext appContext;

    private boolean canContinue=true;
    private boolean isRegistered = false;


    private State state;

    public DirectorySynchronizer(AppContext appContext, Foc d) {
        this.appContext = appContext;
        directory=d;
    }

    /* If the DirectorySynchronizer is active, it may have already updated
     * the summary database, but not yet written the preview files,
     * so it isn't safe to restart it.
     */
    private boolean active = false;
    public synchronized void start() {
        if (!active) {
            if (appContext.getServices().lock()) {
                active = true;
                try {
                    database = openDatabase();
                    setState(new StatePrepareSync());
                } catch (Exception e) {
                    terminate(e);
                }
            }
        }
    }

    private GpxDatabase openDatabase() {
        final String dbPath = appContext.getSummaryConfig().getDBPath(directory);
        final String[] query = {GpxDbConfiguration.KEY_FILENAME};

        dbAccessTime = new File(dbPath).lastModified();
        return new GpxDatabase(appContext.createDataBase(), dbPath, query);
    }

    private synchronized void setReceiver(boolean register, String msg) {
        if (register) {
            appContext.getBroadcaster().register(onFileChanged, msg);
        } else {
            appContext.getBroadcaster().unregister(onFileChanged);
        }
        isRegistered = register;
    }

    private final BroadcastReceiver onFileChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(String... args) {
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
    private final class StatePrepareSync extends State {
        private Exception exception=null;

        private BackgroundTask backgroundTask = new BackgroundTask() {

            @Override
            public long bgOnProcess(AppContext sc) {
                try {
                    filesToAdd = new FilesInDirectory(directory);
                    compareFileSystemWithDatabase();
                    removeFilesFromDatabase();

                } catch (Exception e) {
                    exception = e;

                } finally {
                    backgroundTask = null;

                    sc.getBroadcaster().broadcast(AppBroadcaster.CACHE_SYNCHRONIZED, directory.getPath());
                }

                return 100;

            }
        };


        @Override
        public void start() {
            appContext.getBroadcaster().broadcast(AppBroadcaster.DBSYNC_START);
            setReceiver(true, AppBroadcaster.CACHE_SYNCHRONIZED);
            appContext.getServices().getBackgroundService().process(backgroundTask);
        }


        @Override
        public void ping() {
            setReceiver(false, AppBroadcaster.CACHE_SYNCHRONIZED);
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
                appContext.getBroadcaster().broadcast(AppBroadcaster.DB_SYNC_CHANGED);
            }
        }


        private void removeFileFromDatabase(String name) {
            final Foc file = directory.child(name);

            appContext.getSummaryConfig().getPreviewFile(file).rm();
            database.deleteEntry(file);
        }



        private void compareFileSystemWithDatabase() {
            final DbResultSet resultSet = database.query(null);

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

        private String getFileName(DbResultSet resultSet) {
            return resultSet.getString(GpxDbConfiguration.KEY_FILENAME);
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
                setReceiver(true, AppBroadcaster.FILE_CHANGED_INCACHE);
                Obj h = appContext.getServices().getCacheService().getObject(
                        file.getPath(), new ObjGpxStatic.Factory());
                if (h instanceof ObjGpx) {
                    setPendingGpxHandle((ObjGpx)h);
                    if (h.isReadyAndLoaded()) {
                        state.ping();
                    }

                } else {
                    setReceiver(false, AppBroadcaster.FILE_CHANGED_INCACHE);
                    h.free();
                    state.start();
                }
            }
        }

        @Override
        public void ping() {
            if (!canContinue) {
                terminate();

            } else if (pendingHandle.isReadyAndLoaded()) {
                setReceiver(false, AppBroadcaster.FILE_CHANGED_INCACHE);
                try {
                    addGpxSummaryToDatabase(pendingHandle.getID(), pendingHandle.getGpxList());
                    setState(new StateLoadPreview());
                } catch (Exception e) {
                    terminate(e);
                }

            }  else if (pendingHandle.hasException()) {
                state.start();
            }
        }


        private void addGpxSummaryToDatabase(String id, GpxList list) {
            final Foc file = appContext.toFoc(id);

            ArrayList<String> keys = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            createContentValues(file.getName(), list.getDelta(), keys, values);
            database.insert(Objects.toArray(keys), (Object[]) Objects.toArray(values));
        }

        private void createContentValues(String filename,
                GpxBigDeltaInterface summary, ArrayList<String> keys, ArrayList<String> values) {

            BoundingBoxE6 bounding = summary.getBoundingBox();


            keys.add(GpxDbConfiguration.KEY_FILENAME);   values.add(filename);
            keys.add(GpxDbConfiguration.KEY_AVG_SPEED);  values.add(String.valueOf(summary.getSpeed()));

            keys.add(GpxDbConfiguration.KEY_MAX_SPEED);  values.add(toNumber(summary.getAttributes().get(MaxSpeed.INDEX_MAX_SPEED)));

            keys.add(GpxDbConfiguration.KEY_DISTANCE);   values.add(String.valueOf(summary.getDistance()));
            keys.add(GpxDbConfiguration.KEY_START_TIME); values.add(String.valueOf(summary.getStartTime()));
            keys.add(GpxDbConfiguration.KEY_TOTAL_TIME); values.add(String.valueOf(summary.getTimeDelta()));
            keys.add(GpxDbConfiguration.KEY_END_TIME);   values.add(String.valueOf(summary.getEndTime()));
            keys.add(GpxDbConfiguration.KEY_PAUSE);      values.add(String.valueOf(summary.getPause()));
            keys.add(GpxDbConfiguration.KEY_TYPE_ID);    values.add(String.valueOf(summary.getType().toInteger()));
            keys.add(GpxDbConfiguration.KEY_EAST_BOUNDING); values.add(String.valueOf(bounding.getLonEastE6()));
            keys.add(GpxDbConfiguration.KEY_WEST_BOUNDING); values.add(String.valueOf(bounding.getLonWestE6()));
            keys.add(GpxDbConfiguration.KEY_NORTH_BOUNDING); values.add(String.valueOf(bounding.getLatNorthE6()));
            keys.add(GpxDbConfiguration.KEY_SOUTH_BOUNDING); values.add(String.valueOf(bounding.getLatSouthE6()));
        }

    }

    private String toNumber(String s) {
        if (s.equals("")) return "0";
        return s;
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
            Foc gpxFile = appContext.toFoc(pendingHandle.getID());

            Foc previewImageFile = appContext.getSummaryConfig().getPreviewFile(gpxFile);

            GpxInformation info =
                    new GpxFileWrapper(gpxFile, pendingHandle.getGpxList());


            try {
                setReceiver(true, AppBroadcaster.CACHE_IS_LOADED);
                MapPreviewInterface p = appContext.createMapPreview(info, previewImageFile);

                setPendingPreviewGenerator(p);
                if (p.isLoaded()) {
                    state.ping();
                }

            } catch (Exception e) {
                AppLog.w(this, e);

                appContext.getBroadcaster().broadcast(AppBroadcaster.DB_SYNC_CHANGED);
                setState(new StateLoadNextGpx());
            }
        }

        @Override
        public void ping() {
            if (!canContinue) {
                terminate();
            } else if (pendingPreviewGenerator.isLoaded()) {
                setReceiver(false, AppBroadcaster.CACHE_IS_LOADED);
                pendingPreviewGenerator.generateBitmapFile();

                appContext.getBroadcaster().broadcast(AppBroadcaster.DB_SYNC_CHANGED);
                setState(new StateLoadNextGpx());
            } // else wait for another ping()
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
            synchronized (this) {
                AppLog.d(this, "state terminate");
                if (isRegistered) {
                    appContext.getBroadcaster().unregister(onFileChanged);
                    isRegistered = false;
                }

                if (database != null) {
                    database.close();
                }

                setPendingGpxHandle(null);
                setPendingPreviewGenerator(null);

                appContext.getBroadcaster().broadcast(AppBroadcaster.DBSYNC_DONE);


                appContext.getServices().free();
                active = false;
            }
        }
    }


    @Override
    public synchronized void close() {
        canContinue=false;
        state.ping();
    }
}
