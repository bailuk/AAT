package ch.bailu.aat.services.cache;

import java.io.Closeable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.ServiceLink;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.ObjectHandle.Factory;
import ch.bailu.aat.services.icons.IconMapService;



public class CacheService extends AbsService {

    private ServiceLink serviceLink = null;    

    public static final Class<?> SERVICES[] = {
        BackgroundService.class,
        IconMapService.class,
    };    


    
    private Self self = new Self();;

    public Self getSelf() {
        return self;

    }

    @Override
    public void onCreate() {
        serviceLink = new ServiceLink(this) {
            @Override
            public Self getCacheService() {
                return getSelf();
            }

            @Override
            public void onServicesUp() {
                self.close();
                self = new SelfOn(this);
            }
        };

        serviceLink.up(SERVICES);
        super.onCreate();
    }



    @Override
    public void onLowMemory() {
        self.onLowMemory();
    }

    

    @Override
    public void appendStatusText(StringBuilder builder) {
        super.appendStatusText(builder);
        self.appendStatusText(builder);
    }


    
    @Override
    public void onDestroy() {
        self.close();
        self = new Self();
        
        serviceLink.close();
        serviceLink=null;
        super.onDestroy();
    }
    
    
    public static class Self implements Closeable {
        @Override
        public void close() {}

        public void addToBroadcaster(ObjectBroadcastReceiver b) {}
        public void onLowMemory() {}

        public ObjectHandle getObject(String id) {
            return ObjectHandle.NULL;
        }

        public ObjectHandle getObject(String id, Factory factory) {
            return ObjectHandle.NULL;
        }

        public void appendStatusText(StringBuilder builder) {}
    }


    public class SelfOn extends Self {
        public final ObjectTable table=new ObjectTable();
        public final ObjectBroadcaster broadcaster;

        public final ServiceContext serviceContext;

        public SelfOn(ServiceContext sc) {
            broadcaster = new ObjectBroadcaster(sc);
            serviceContext=sc;

            AppBroadcaster.register(sc.getContext(), onFileProcessed, AppBroadcaster.FILE_CHANGED_INCACHE);
        }

        @Override
        public void onLowMemory() {
            table.onLowMemory(this);
        }

        @Override
        public ObjectHandle getObject(String id, ObjectHandle.Factory factory) {
            return table.getHandle(id, factory, this);
        }

        @Override
        public ObjectHandle getObject(String id) {
            return table.getHandle(id, serviceContext);
        }

        @Override
        public void appendStatusText(StringBuilder builder) {
            super.appendStatusText(builder);
            table.appendStatusText(builder);
        }


        @Override
        public void close() {
            unregisterReceiver(onFileProcessed);
            broadcaster.close();
        }


        @Override
        public void addToBroadcaster(ObjectBroadcastReceiver b) {
            broadcaster.put(b);
        }
        
        private BroadcastReceiver onFileProcessed = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                table.onObjectChanged(intent, SelfOn.this);
            }
        };
    }


    @Override
    public void onServicesUp() {}
}
