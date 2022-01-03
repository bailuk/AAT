package ch.bailu.aat_lib.service.directory;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.VirtualService;
import ch.bailu.aat_lib.util.fs.AFile;
import ch.bailu.aat_lib.util.sql.DbException;
import ch.bailu.aat_lib.util.sql.DbResultSet;
import ch.bailu.foc.Foc;

public final class DirectoryService extends VirtualService implements OnPreferencesChanged, DirectoryServiceInterface {


    private AbsDatabase database=AbsDatabase.NULL_DATABASE;
    private final SolidDirectoryQuery sdirectory;
    private DirectorySynchronizer synchronizer=null;

    private final AppContext appContext;

    public DirectoryService(AppContext appContext) {
        this.appContext = appContext;
        sdirectory = new SolidDirectoryQuery(appContext.getStorage(), appContext);
        sdirectory.getStorage().register(this);

        openDir(sdirectory.getValueAsFile());
    }

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {
        if (sdirectory.hasKey(key)) {
            openDir(sdirectory.getValueAsFile());
        }
    }

    private void openDir(Foc dir) {

        if (dir.mkdirs() && dir.canRead()) {
            open(dir);

        } else {
            AFile.logErrorNoAccess(dir);
        }
    }


    private void open(Foc dir) {
        final String dbPath = appContext.getSummaryConfig().getDBPath(dir);

        try {
            openDataBase(dbPath);

        } catch (Exception e) {
            database=AbsDatabase.NULL_DATABASE;
        }
    }



    private void openDataBase(String dbPath) throws DbException {
        database.close();
        database = new GpxDatabase(appContext.createDataBase(), dbPath);
    }



    public DbResultSet query(String sel) {
        return database.query(sel);
    }



    public void deleteEntry(Foc file)  {
        database.deleteEntry(file);
        rescan();
    }


    public void rescan() {
        rescan(sdirectory.getValueAsFile());
    }


    private void rescan(Foc dir) {
        if (dir.canRead()) {
            stopSynchronizer();
            synchronizer = new DirectorySynchronizer(appContext,dir);
        }
    }


    private void stopSynchronizer() {
        if (synchronizer != null) {
            synchronizer.close();
            synchronizer=null;
        }
    }


    public void close() {
        database.close();
        sdirectory.getStorage().unregister(this);
        stopSynchronizer();
    }


    @Override
    public void appendStatusText(StringBuilder builder) {
        builder.append("<p>Directory: ");
        builder.append(sdirectory.getValueAsString());
        builder.append("</p>");
    }
}
