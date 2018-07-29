package ch.bailu.aat.services.directory;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;

import java.io.IOException;

import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.util.fs.AFile;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;

public class DirectoryService extends VirtualService implements OnSharedPreferenceChangeListener{


    private AbsDatabase database=AbsDatabase.NULL_DATABASE;
    private final SolidDirectoryQuery sdirectory;
    private DirectorySynchronizer synchronizer=null;


    public DirectoryService(ServiceContext scontext) {
        super(scontext);
        sdirectory = new SolidDirectoryQuery(getContext());
        sdirectory.getStorage().register(this);

        openDir(sdirectory.getValueAsFile());
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        if (sdirectory.hasKey(key)) {
            openDir(sdirectory.getValueAsFile());
        }
    }



    private void openDir(Foc dir) {

        if (dir.mkdirs() && dir.canRead()) {
            open(dir);

        } else {
            AFile.logErrorNoAccess(getContext(), dir);
        }
    }





    private void open(Foc dir) {
        final String dbPath =
                SummaryConfig.getWriteableDBPath(getContext(), dir);

        try {
            openDataBase(getSContext(), dbPath);

        } catch (Exception e) {
            database=AbsDatabase.NULL_DATABASE;
        }
    }



    private void openDataBase(ServiceContext sc, String dbPath) throws IOException, SQLiteCantOpenDatabaseException {
        database.close();
        database = new GpxDatabase(
                sc,
                dbPath);
    }



    public Cursor query(String sel) {
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
            synchronizer = new DirectorySynchronizer(getSContext(), dir);
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
