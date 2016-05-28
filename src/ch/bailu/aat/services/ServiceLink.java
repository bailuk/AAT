package ch.bailu.aat.services;

import java.io.Closeable;
import java.util.Hashtable;
import java.util.Iterator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.dem.ElevationService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.editor.EditorService;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.services.tracker.TrackerService;

public abstract class ServiceLink extends ServiceContext implements Closeable {

    public static final Class<?> ALL_SERVICES[] = {
        // Service:                Dependencies:
        TrackerService.class,   // none

        BackgroundService.class,// none
        IconMapService.class,   // none

        CacheService.class,     // background and iconmap

        DirectoryService.class, // cache and background

        ElevationService.class, // cache and background

        EditorService.class,    // cache
    }; 


    private class Connection implements ServiceConnection, Closeable {
        private AbsService service=null;
        private final Context context;

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
        public void close() {
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


    public abstract void onServicesUp();


    private final Hashtable<Class<?>, Connection> serviceTable = 
            new Hashtable<Class<?>, Connection>();


    private final Context context;
    public ServiceLink(Context c) {
        context=c; 
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void up(Class<?>[] services) {
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

    private boolean isServiceUp(Class<?> s) {
        if (serviceTable.containsKey(s)) {
            return serviceTable.get(s).isUp();
        }
        return false;
    }

    @Override
    public AbsService getService(Class<?> s) throws ServiceNotUpException {
        if (isServiceUp(s))
            return serviceTable.get(s).getService();
        else 
            throw new ServiceNotUpException(s);
    }




    public void down() {
        Iterator<Connection> iterator = serviceTable.values().iterator();

        while (iterator.hasNext()) {
            Connection current = iterator.next();
            current.close();
        }
        serviceTable.clear();
    }

    @Override
    public void close() {
        down();
    }
}
