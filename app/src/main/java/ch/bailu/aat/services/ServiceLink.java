package ch.bailu.aat.services;

import java.io.Closeable;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public abstract class ServiceLink extends ServiceContext implements ServiceConnection, Closeable {

    private OneService service=null;
    private boolean bound =false;


    private final Context context;


    public ServiceLink(Context c) {
        context=c;
    }


    @Override
    public Context getContext() {
        return context;
    }


    public void up() {
        if (bound == false) {
            bound = true;
            context.bindService(new Intent(context,
                    OneService.class), this, Context.BIND_AUTO_CREATE);
        }
    }


    @Override
    public void onServiceConnected(ComponentName className, IBinder binder) {

        service =  (OneService)((AbsService.CommonBinder)binder).getService();
        service.lock(ServiceLink.class.getSimpleName());
        onServiceUp();
    }


    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }



    public void down() {
        if (service != null) {
            service.free(ServiceLink.class.getSimpleName());
        }

        if (bound) {
            bound = false;
            context.unbindService(this);
        }
    }


    public boolean isUp() {
        return service != null;
    }

    public abstract void onServiceUp();


    @Override
    public OneService getService() throws ServiceNotUpException {
        if (isUp())
            return service;
        else
            throw new ServiceNotUpException(OneService.class);
    }


    @Override
    public void lock(String s) {
        if (isUp()) service.lock(s);
    }


    @Override
    public void free(String s) {
        if (isUp()) service.free(s);
    }

    @Override
    public void close() {
        down();
    }
}
