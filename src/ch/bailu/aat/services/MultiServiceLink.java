package ch.bailu.aat.services;

import java.util.Hashtable;
import java.util.Iterator;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import ch.bailu.aat.helpers.CleanUp;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.editor.EditorService;
import ch.bailu.aat.services.overlay.OverlayService;
import ch.bailu.aat.services.srtm.ElevationService;
import ch.bailu.aat.services.tracker.TrackerService;

public abstract class MultiServiceLink implements CleanUp {
    public static final Class<?> ALL_SERVICES[] = {
        TrackerService.class, 
        DirectoryService.class, 
        CacheService.class,
        BackgroundService.class,
        OverlayService.class,
        ElevationService.class,
        EditorService.class};


    public static final Class<?> LOADER_SERVICES[] = {
        CacheService.class
    };
    
    

    public static final MultiServiceLink NULL_SERVICE_LINK= new MultiServiceLink() {
        @Override
        public void onServicesUp() {}

    };


    public class ServiceNotUpException extends Exception {
        private static final long serialVersionUID = 5632759660184034845L;

        public ServiceNotUpException(Class<?> service)	{
            super("Service '" + Service.class.getSimpleName() + "' is not running.*");
        }
    }


    private class Connection implements ServiceConnection, CleanUp {
        private AbsService service=null;
        private Context context;

        public Connection(Context c, Class<?> serviceClass) {
            context = c;

            context.bindService(new Intent(context, 
                    serviceClass), this, Context.BIND_AUTO_CREATE);
        }

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            service =  ((AbsService.CommonBinder)binder).getService();

            if (areAllServicesUp()) {
                onServicesUp();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }

        @Override
        public void cleanUp() {
            context.unbindService(this);
            service=null;
        }

        public boolean isUp() {
            return service != null;
        }

        public AbsService getService() {
            return service;
        }
    };


    private Hashtable<Class<?>, Connection> serviceTable = 
            new Hashtable<Class<?>, Connection>();


    private MultiServiceLink() {	}

    public MultiServiceLink(Context context, Class<?>[] services) {
        for (Class<?> s: services) {
            serviceTable.put(s, new Connection(context, s));
        }
    }


    public boolean areAllServicesUp() {
        boolean r=true;
        Iterator<Connection> iterator = serviceTable.values().iterator();

        while (iterator.hasNext()) {
            Connection current = iterator.next();

            if (!current.isUp()) {
                r=false;
                break;
            }
        }

        return r;
    }

    public abstract void onServicesUp();

    @Override
    public void cleanUp() {
        Iterator<Connection> iterator = serviceTable.values().iterator();

        while (iterator.hasNext()) {
            Connection current = iterator.next();
            current.cleanUp();
        }
    }

    public boolean isServiceUp(Class<?> s) {
        if (serviceTable.containsKey(s)) {
            return serviceTable.get(s).isUp();
        }
        return false;
    }

    public AbsService getService(Class<?> s) throws ServiceNotUpException {
        if (isServiceUp(s))
            return serviceTable.get(s).getService();
        else 
            throw new ServiceNotUpException(s);
    }
}
