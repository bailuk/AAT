package ch.bailu.aat.services.directory;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public class DirectoryService extends VirtualService{


    private final Self self;


    public Self getSelf() {
        return self;
    }



    public DirectoryService(ServiceContext sc) {
        super(sc);
        self = new SelfOn();
    }



    @Override
    public void close() {
        self.close();
    }


    public static class Self implements Closeable{
        public void rescan() {}
        public void deleteEntry(File file)  {}


        @Override
        public void close() {}
        public void appendStatusText(StringBuilder builder) {}
        
        public Cursor query(String selection) {
            return null;
        }
    }



    public class SelfOn extends Self implements OnSharedPreferenceChangeListener {
        private AbsDatabase database=AbsDatabase.NULL_DATABASE;
        private final SolidDirectoryQuery sdirectory;
        private DirectorySynchronizer synchronizer=null;


        public SelfOn() {
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
                if (isDirWriteable()) {
                    rescan();
                } else {
                    logReadOnly();
                }
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

        
        @Override
        public Cursor query(String sel) {
            return database.query(sel);
        }
        

        @Override
        public void deleteEntry(File file)  {
            database.deleteEntry(file);
            rescan();
        }

        
        @Override
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

        @Override
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

    @Override
    public void appendStatusText(StringBuilder builder) {
        self.appendStatusText(builder);
    }
}
