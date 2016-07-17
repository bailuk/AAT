package ch.bailu.aat.services.directory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.preferences.SolidDirectory;
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
        public void deleteEntry(String fileID)  {}


        @Override
        public void close() {}
        public void appendStatusText(StringBuilder builder) {}
        
        public Cursor query(String selection) {
            return null;
        }
    }



    public class SelfOn extends Self implements OnSharedPreferenceChangeListener {
        private AbsDatabase database=AbsDatabase.NULL_DATABASE;
        private final SolidDirectory  sdirectory;
        private DirectorySynchronizer synchronizer=null;


        public SelfOn() {
            sdirectory = new SolidDirectory(getContext());
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
            if (isDirValid()) {
                open();
                rescan();
            }
        }
        
        
        private void open() {
            try {
                final File db = AppDirectory.getCacheDb(getDir());
                openDataBase(getSContext(), db);

            } catch (IOException e) {
                database=AbsDatabase.NULL_DATABASE;
            } 
        }



        private File getDir() {
            return new File(sdirectory.getValue());
        }

        
        private boolean isDirValid() {
            return (getDir().exists());
        }



        private void openDataBase(ServiceContext sc, File path) throws IOException {
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
        public void deleteEntry(String fileID)  {
            database.deleteEntry(fileID);
            rescan();
        }

        
        @Override
        public void rescan() {
            if (isDirValid()) {
                stopSynchronizer();
                synchronizer = new DirectorySynchronizer(getSContext(), new File(sdirectory.getValue()));
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
            builder.append(sdirectory.getValue());
            builder.append("</p>");                
        }

    }

    @Override
    public void appendStatusText(StringBuilder builder) {
        self.appendStatusText(builder);
    }
}
