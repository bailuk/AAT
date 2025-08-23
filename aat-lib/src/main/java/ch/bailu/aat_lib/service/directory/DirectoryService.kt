package ch.bailu.aat_lib.service.directory;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.VirtualService;
import ch.bailu.aat_lib.service.directory.database.AbsDatabase;
import ch.bailu.aat_lib.service.directory.database.GpxDatabase;
import ch.bailu.aat_lib.util.fs.AFile;
import ch.bailu.aat_lib.util.sql.DbException;
import ch.bailu.aat_lib.util.sql.DbResultSet;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocName;

public final class DirectoryService extends VirtualService implements DirectoryServiceInterface {
    private AbsDatabase database = AbsDatabase.NULL_DATABASE;
    private Foc directory = new FocName("");
    private DirectorySynchronizer synchronizer = null;

    private final AppContext appContext;

    public DirectoryService(AppContext appContext) {
        this.appContext = appContext;
    }

    public void openDir(Foc dir) {
        if (dir.mkdirs() && dir.canRead()) {
            directory = dir;
            open(directory);
            rescan(directory);
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
        rescan(directory);
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
        stopSynchronizer();
    }

    @Override
    public void appendStatusText(StringBuilder builder) {
        builder.append("<p>Directory: ");
        builder.append(directory.getPathName());
        builder.append("</p>");
    }
}
