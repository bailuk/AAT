package ch.bailu.aat.services.cache;

import java.io.Closeable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.MultiServiceLink.ServiceContext;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.ObjectHandle.Factory;
import ch.bailu.aat.services.icons.IconMapService;



public class CacheService extends AbsService {
    public static final Class<?> SERVICES[] = {
        BackgroundService.class,
        IconMapService.class
    };    


    public final static Self NULL_SELF=new Self();
    
    private Self self = NULL_SELF;
    
    public Self getSelf() {
        return self;
        
    }
    
    public static class Self implements Closeable {
        @Override
        public void close() {
        }

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

        public SelfOn(ServiceContext cs) {
            broadcaster = new ObjectBroadcaster(cs);
            serviceContext=cs;
            
            AppBroadcaster.register(cs.getContext(), onFileProcessed, AppBroadcaster.FILE_CHANGED_INCACHE);
        }
        
        @Override
        public void onLowMemory() {
            table.onLowMemory(this);
        }

        @Override
        public ObjectHandle getObject(String id, ObjectHandle.Factory factory) {
            ObjectHandle handle = table.getHandle(id, factory, this);
            return handle;
        }

        @Override
        public ObjectHandle getObject(String id) {
            ObjectHandle handle = table.getHandle(id, serviceContext);
            return handle;
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
    public void onCreate() {
        connectToServices(SERVICES);
        
        super.onCreate();
    }


    
    @Override
    public void onServicesUp() {
        
        BackgroundService.Self background=null;
        IconMapService    iconMap=null;
        
        try {
            background =  getServiceContext().getBackgroundService();
            iconMap = getServiceContext().getIconMapService();
            
        } catch (ServiceNotUpException e) {
            e.printStackTrace();
        }
        
        
        if (background != null && iconMap != null)
            self.close();
            self = new SelfOn(getServiceContext());
    }

    
    
    @Override
    public void onLowMemory() {
        self.onLowMemory();
    }
    
    public ObjectHandle getObject(String id, ObjectHandle.Factory factory) {
        return self.getObject(id, factory);
    }

    
    public ObjectHandle getObject(String id) {
        return self.getObject(id);
    }

    @Override
    public void appendStatusText(StringBuilder builder) {
        super.appendStatusText(builder);
        self.appendStatusText(builder);
    }
    

    @Override
    public void onDestroy() {
        self.close();
        self = NULL_SELF;
        super.onDestroy();
    }
}
