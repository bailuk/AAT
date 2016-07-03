package ch.bailu.aat.services.directory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.preferences.SolidInteger;
import ch.bailu.aat.preferences.SolidString;
import ch.bailu.aat.preferences.Storage;
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
        public void reopen(File f, String s) {}
        public void rescan() {}
        public void deleteCurrentTrackFromDb()  {}

        public int size() {
            return 0;
        }

        public void setPosition(int i) {}
        public void toNext() {}
        public void toPrevious() {}

        public GpxInformation getCurrent() {
            return GpxInformation.NULL;  
        }

        public GpxInformation getListSummary() {
            return GpxInformation.NULL;
        }

        public int getPosition() {
            return 0;
        }

        @Override
        public void close() {}
        public void appendStatusText(StringBuilder builder) {}
    }



    public class SelfOn extends Self {

        private final String KEY_DIR_DIRECTORY="DIR_DIRECTORY";
        private final String KEY_DIR_SELECTION="DIR_SELECTION_"; // + directory
        private final String KEY_DIR_INDEX="DIR_INDEX_"; // + directory


        private AbsDatabase database=AbsDatabase.NULL_DATABASE;

        private final SolidString  directory;
        private       SolidString  selection;
        private       SolidInteger index;



        private DirectorySynchronizer synchronizer=null;


        private BroadcastReceiver           onSyncChanged = new BroadcastReceiver () {
            @Override
            public void onReceive(Context context, Intent intent) {
                database.reopenCursor();
                AppBroadcaster.broadcast(context, AppBroadcaster.DB_CURSOR_CHANGED);
            }
        };



        public SelfOn() {
            directory = new SolidString(Storage.global(getContext()), KEY_DIR_DIRECTORY);
            AppBroadcaster.register(getContext(), onSyncChanged, AppBroadcaster.DB_SYNC_CHANGED);

            if (setDirectory()) {
                open();
                rescan();
            }
        }


        @Override
        public void reopen(File dir, String sel) {
            if (setDirectory(dir.getAbsolutePath(), sel)) {
                open();
                rescan();
                AppBroadcaster.broadcast(getContext(), AppBroadcaster.DB_CURSOR_CHANGED);
            }
        }
        
        
        private boolean setDirectory(String dir, String sel) {
            directory.setValue(dir);
            
            if (setDirectory()) {
                selection.setValue(sel);
                return true;
            }
            return false;
        }
        
        
        private boolean setDirectory() {
            selection = new SolidString(Storage.global(getContext()), KEY_DIR_SELECTION+directory.getValue());
            selection = new SolidString(Storage.global(getContext()), KEY_DIR_SELECTION+directory.getValue());
            index     = new SolidInteger(Storage.global(getContext()), KEY_DIR_INDEX+directory.getValue());

            return isDirValid();
        }
        
        
        
        private void open() {
            try {
                final File db = AppDirectory.getCacheDb(getDir());
                openDataBase(getSContext(), db, selection.getValue());

            } catch (IOException e) {
                database=AbsDatabase.NULL_DATABASE;
            } 
        }



        private File getDir() {
            return new File(directory.getValue());
        }

        private boolean isDirValid() {
            return (getDir().exists());
        }


/*
        private void requery(final String q) {
            if (isDirValid()) {
                selection.setValue(q);
                requery();
            }
        }


        private void requery() {
            database.reopenCursor(selection.getValue());
            AppBroadcaster.broadcast(getContext(), AppBroadcaster.DB_CURSOR_CHANGED);
        }

*/


        private void openDataBase(ServiceContext sc, File path, String selection) throws IOException {
            database.close();
            database = new GpxDatabase(
                    sc, 
                    path,
                    selection);
        }


        @Override
        public void deleteCurrentTrackFromDb()  {
            database.deleteEntry(getCurrent().getPath());
            rescan();
        }

        @Override
        public void rescan() {
            if (isDirValid()) {
                stopSynchronizer();
                synchronizer = new DirectorySynchronizer(getSContext(), new File(directory.getValue()));
            }
        }


        private void stopSynchronizer() {
            if (synchronizer != null) {
                synchronizer.close();
                synchronizer=null;
            }
        }


        @Override
        public int size() {
            return database.getIterator().size();
        }


        @Override
        public void setPosition(int i) {
            database.getIterator().setPosition(i);
            index.setValue(database.getIterator().getPosition());
        }


        @Override
        public void toNext() {
            database.getIterator().setPosition(database.getIterator().getPosition()+1);
            index.setValue(database.getIterator().getPosition());
        }


        @Override
        public void toPrevious() {
            database.getIterator().setPosition(database.getIterator().getPosition()-1);
            index.setValue(database.getIterator().getPosition());
        }


        @Override
        public GpxInformation getCurrent() {
            return database.getIterator();  
        }


        @Override
        public GpxInformation getListSummary() {
            return database.getIterator().getListSummary();
        }


        @Override
        public int getPosition() {
            return index.getValue(); 
        }


        @Override
        public void close() {
            getContext().unregisterReceiver(onSyncChanged);

            database.close();
            stopSynchronizer();
        }


        @Override
        public void appendStatusText(StringBuilder builder) {
            builder.append("<p>Directory: ");
            builder.append(directory.getValue());
            builder.append("<br>Selection: ");
            builder.append(selection.getValue());
            builder.append("<br>Index: ");
            builder.append(index.getValue());
            builder.append("</p>");                
        }
    }


    @Override
    public void appendStatusText(StringBuilder builder) {
        self.appendStatusText(builder);
    }
}
