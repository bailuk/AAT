package ch.bailu.aat.services;

import java.io.Closeable;
import java.util.Hashtable;
import java.util.Iterator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public abstract class ServiceLink extends ServiceContext implements Closeable {

    static int connections=0;

    private class Connection implements ServiceConnection, Closeable {
        
        private AbsService service=null;
        private final Context context;

        public Connection(Context c, Class<?> serviceClass) {
            connections++;
            
            context = c;
            context.bindService(new Intent(context, 
                    serviceClass), this, Context.BIND_AUTO_CREATE);
        }

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            
            service =  ((AbsService.CommonBinder)binder).getService();

            if (areAllServicesUp()) {
                onServicesUp();
                service.lock(ServiceLink.class.getSimpleName());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            
            service = null;
        }

        @Override
        public void close() {
            connections--;
            
            if (connections==0 && service != null) 
                service.free(ServiceLink.class.getSimpleName());
            
            context.unbindService(this);
            service=null;
        }

        public boolean isUp() {
            return service != null;
        }

        public AbsService getService() {
            return service;
        }
    }


    public abstract void onServicesUp();


    private final Hashtable<Class<?>, Connection> serviceTable =
            new Hashtable<>();


    private final Context context;
    public ServiceLink(Context c) {
        context=c; 
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void up() {
        serviceTable.put(OneService.class, new Connection(context, OneService.class));

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
    public OneService getService() throws ServiceNotUpException {
        if (isServiceUp(OneService.class))
            return (OneService)serviceTable.get(OneService.class).getService();
        else 
            throw new ServiceNotUpException(OneService.class);
    }

    
    @Override
    public void lock(String s) {
        try {
            getService().lock(s);
        } catch (ServiceNotUpException e) {}
    }
    
    
    @Override
    public void free(String s) {
        try {
            getService().free(s);
        } catch (ServiceNotUpException e) {}
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
