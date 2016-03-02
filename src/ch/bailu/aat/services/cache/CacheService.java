package ch.bailu.aat.services.cache;

import java.io.Closeable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.ObjectHandle.Factory;
import ch.bailu.aat.services.icons.IconMapService;



public class CacheService extends AbsService {
    public static final Class<?> SERVICES[] = {
        BackgroundService.class,
        IconMapService.class
    };    


    
    public class Self implements Closeable {
        @Override
        public void close() {
        }

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
        private final ObjectTable table=new ObjectTable();
        public final ObjectBroadcaster broadcaster;
        public final BackgroundService background;
        public final IconMapService    iconMap;
        public final Context context;
        

        public SelfOn(BackgroundService bg, IconMapService im) {
            iconMap=im;
            background = bg;
            context = CacheService.this;
            broadcaster = new ObjectBroadcaster(this);
            
            
            AppBroadcaster.register(context, onFileProcessed, AppBroadcaster.FILE_CHANGED_INCACHE);
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
            ObjectHandle handle = table.getHandle(id, this);
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
        
        
        private BroadcastReceiver onFileProcessed = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                table.onObjectChanged(intent, SelfOn.this);
            }
        };
        
    }
    
    
    private Self self = new Self();
    
    @Override
    public void onCreate() {
        connectToServices(SERVICES);
        
        super.onCreate();
    }


    
    @Override
    public void onServicesUp() {
        
        BackgroundService background=null;
        IconMapService    iconMap=null;
        
        try {
            background = (BackgroundService) getService(BackgroundService.class);
            iconMap = (IconMapService) getService(IconMapService.class);
            
        } catch (ServiceNotUpException e) {
            e.printStackTrace();
        }
        
        
        if (background != null && iconMap != null)
            self.close();
            self = new SelfOn(background, iconMap);
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
        self = new Self();
        super.onDestroy();
    }
}
