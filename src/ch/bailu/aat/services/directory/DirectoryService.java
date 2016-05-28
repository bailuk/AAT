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
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.ServiceContext.ServiceNotUpException;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;

public class DirectoryService extends AbsService	{


    private static final Class<?> SERVICES[] = {BackgroundService.class, CacheService.class};


    private Self self = new Self();


    public Self getSelf() {
        return self;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        connectToServices(SERVICES);
    }


    @Override
    public void onServicesUp() {
        self = new SelfOn();
    }

    @Override
    public void onDestroy() {
        self.close();
        self = new Self();
        super.onDestroy();
    }

    
    public static class Self implements Closeable{
        public void setDirectory(final File directory, final String selection) {
            AppLog.d(this, "fuck you!!");

        };
        public void setSelection(String selection) {}

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


        public int getStoredPosition() {
            return 0; 
        }

        public void storePosition() {}



        public void storePosition(int position) {}


        @Override
        public void close() {}
    }


    public class SelfOn extends Self {
        private AbsDatabase database=AbsDatabase.NULL_DATABASE;
        private String positionKey="directory_serivce";

        private String pendingSelection=null;
        private File   pendingDirectory=null;

        private DirectorySynchronizer synchronizer=null;


        public SelfOn() {
            AppBroadcaster.register(DirectoryService.this, onSyncChanged, AppBroadcaster.DB_SYNC_CHANGED);            
            openPending();
        }


        private BroadcastReceiver           onSyncChanged = new BroadcastReceiver () {
            @Override
            public void onReceive(Context context, Intent intent) {
                database.reopenCursor();
                AppBroadcaster.broadcast(context, AppBroadcaster.DB_CURSOR_CHANGED);
            }
        };


        public void setDirectory(final File directory, final String selection) {
            pendingSelection=selection;
            pendingDirectory=directory;

            openPending();
        };



        private void openPending() {
            try {
                CacheService.Self objectCache = getServiceContext().getCacheService();


                if (pendingSelection!=null) {

                    if (pendingDirectory!=null) {
                        openDataBase(objectCache, AppDirectory.getCacheDb(pendingDirectory), pendingSelection);

                        startSynchronizer(getServiceContext(), pendingDirectory);
                        positionKey = pendingDirectory.getName();

                    } else  {
                        database.reopenCursor(pendingSelection);
                    }
                }
                pendingDirectory=null;
                pendingSelection=null;
                AppBroadcaster.broadcast(DirectoryService.this, AppBroadcaster.DB_CURSOR_CHANGED);

            } catch (Exception e) {

            }
        }



        public void setSelection(String selection) {
            pendingSelection=selection;
            openPending();
        }


        private void openDataBase(CacheService.Self loader, File path, String selection) throws IOException, ServiceNotUpException {
            database.close();
            database = new GpxDatabase(
                    DirectoryService.this,
                    loader, 
                    path,
                    selection);
        }


        public void deleteCurrentTrackFromDb()  {
            database.deleteEntry(getCurrent().getPath());
        }

        private void startSynchronizer(ServiceContext cs, File directory) throws IOException, ServiceNotUpException {
            stopSynchronizer();
            synchronizer = new DirectorySynchronizer(cs, directory);
        }


        private void stopSynchronizer() {
            if (synchronizer != null) {
                synchronizer.close();
                synchronizer=null;
            }
        }


        public int size() {
            return database.getIterator().size();
        }



        public void setPosition(int i) {
            database.getIterator().setPosition(i);
        }


        public void toNext() {
            database.getIterator().setPosition(database.getIterator().getPosition()+1);
        }


        public void toPrevious() {
            database.getIterator().setPosition(database.getIterator().getPosition()-1);
        }


        public GpxInformation getCurrent() {
            return database.getIterator();  
        }


        public GpxInformation getListSummary() {
            return database.getIterator().getListSummary();
        }


        public int getStoredPosition() {
            return Storage.global(DirectoryService.this).readInteger(positionKey); 
        }

        public void storePosition() {
            storePosition(database.getIterator().getPosition());

        }



        public void storePosition(int position) {
            Storage.global(DirectoryService.this).writeInteger(positionKey, position);
        }

        @Override
        public void close() {
            unregisterReceiver(onSyncChanged);

            database.close();
            stopSynchronizer();
        }

    }






}
