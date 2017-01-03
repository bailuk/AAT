package ch.bailu.aat.services.directory;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public class DirectoryService extends VirtualService implements OnSharedPreferenceChangeListener{


    private AbsDatabase database=AbsDatabase.NULL_DATABASE;
    private final SolidDirectoryQuery sdirectory;
    private DirectorySynchronizer synchronizer=null;


    public DirectoryService(ServiceContext scontext) {
        super(scontext);
        sdirectory = new SolidDirectoryQuery(getContext());
        sdirectory.getStorage().register(this);

        openDir();
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        if (sdirectory.hasKey(key)) {
            openDir();
        }
    }



    private void openDir() {
        if (isDirReadable()) {
            open();
        } else {
            logNoAccess();
        }
    }


    private void logReadOnly() {
        AppLog.e(getContext(), getDir().getAbsolutePath() + " is read only.*");
    }


    private void logNoAccess() {
        AppLog.e(getContext(), getDir().getAbsolutePath() + " no access.*");
    }



    private void open() {
        final File db = AppDirectory.getCacheDb(getDir());

        try {
            openDataBase(getSContext(), db);

        } catch (Exception e) {
            database=AbsDatabase.NULL_DATABASE;
        }
    }



    private File getDir() {
        return new File(sdirectory.getValueAsString());
    }


    private boolean isDirReadable() {
        return getDir().canRead();
    }

    private boolean isDirWriteable() {
        return getDir().canWrite();
    }




    private void openDataBase(ServiceContext sc, File path) throws IOException, SQLiteCantOpenDatabaseException {
        database.close();
        database = new GpxDatabase(
                sc,
                path);
    }



    public Cursor query(String sel) {
        return database.query(sel);
    }



    public void deleteEntry(File file)  {
        if (isDirWriteable()) {
            database.deleteEntry(file);
            rescan();
        } else {
            logReadOnly();
        }
    }



    public void rescan() {
        if (isDirReadable()) {
            stopSynchronizer();
            synchronizer = new DirectorySynchronizer(getSContext(), new File(sdirectory.getValueAsString()));
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
