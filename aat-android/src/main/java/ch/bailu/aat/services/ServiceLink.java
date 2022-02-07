package ch.bailu.aat.services;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.io.Closeable;

import ch.bailu.aat_lib.service.elevation.ElevationService;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.service.IconMapServiceInterface;
import ch.bailu.aat_lib.service.background.BackgroundServiceInterface;
import ch.bailu.aat_lib.service.cache.CacheServiceInterface;
import ch.bailu.aat_lib.service.directory.DirectoryServiceInterface;
import ch.bailu.aat_lib.service.location.LocationServiceInterface;
import ch.bailu.aat_lib.service.render.RenderServiceInterface;
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface;
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface;

public abstract class ServiceLink implements
        ServiceContext,
        ServiceConnection,
        Closeable {



    public static class ServiceNotUpError extends Error {
        private static final long serialVersionUID = 5632759660184034845L;

        public ServiceNotUpError(Class<?> service)  {
            super("Service '" + service.getSimpleName() + "' is not running.*");
        }
    }


    private int lock = 0;

    private ServiceContext service= null;
    private boolean bound = false;

    private final Context context;



    public ServiceLink(Context c) {
        context=c;
    }


    @Override
    public Context getContext() {
        return context;
    }


    public void up() {
        bindService();
    }



    @Override
    public void onServiceConnected(ComponentName className, IBinder binder) {
        grabService(binder);
        onServiceUp();
    }


    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }



    public void down() {
        releaseService();
        unbindService();
    }


    private void grabService(IBinder binder) {
        service = (ServiceContext)((AbsService.CommonBinder)binder).getService();
        service.lock(ServiceLink.class.getSimpleName());
    }

    private synchronized void releaseService() {
        try {
            while (service != null) {

                if (lock == 0) {
                    service.free(ServiceLink.class.getSimpleName());
                    service = null;

                } else {
                    wait();
                }
            }
        } catch (Exception e) {
                AppLog.e(context, e);
        }
    }


    private void bindService() {
        if (bound == false) {
            bound = true;

            try {
                context.bindService(new Intent(context,
                        OneService.class), this, Context.BIND_AUTO_CREATE);
            } catch (Exception e) {
                AppLog.e(context, e);
            }
        }
    }


    private void unbindService() {
        if (bound) {
            bound = false;

            try {
                context.unbindService(this);
            } catch (Exception e) {
                AppLog.e(context, e);
            }
        }
    }



    private boolean isUp() {
        return service != null;
    }

    public abstract void onServiceUp();



    private ServiceContext getService()  {
        if (isUp())
            return service;
        else
            throw new ServiceNotUpError(OneService.class);
    }


    @Override
    public synchronized boolean lock() {
        if (isUp() && getService().lock()) {
            lock++;
            return true;
        }

        return false;
    }


    @Override
    public synchronized void free() {
        if (isUp()) {
            getService().free();
            lock--;
            if (lock == 0) notifyAll();
        }
    }


    @Override
    public synchronized void lock(String s) {
        if (isUp()) getService().lock(s);
    }


    @Override
    public synchronized void free(String s) {
        if (isUp()) getService().free(s);
    }



    @Override
    public void close() {
        down();
    }


    @Override
    public TrackerServiceInterface getTrackerService() {
        return getService().getTrackerService();
    }

    @Override
    public LocationServiceInterface getLocationService() {return getService().getLocationService(); }

    @Override
    public BackgroundServiceInterface getBackgroundService() {
        return getService().getBackgroundService();
    }

    @Override
    public CacheServiceInterface getCacheService() {
        return getService().getCacheService();
    }

    @Override
    public ElevationService getElevationService() {
        return getService().getElevationService();
    }

    @Override
    public IconMapServiceInterface getIconMapService() {
        return getService().getIconMapService();
    }

    @Override
    public DirectoryServiceInterface getDirectoryService() {
        return getService().getDirectoryService();
    }

    @Override
    public RenderServiceInterface getRenderService() { return getService().getRenderService();}


    @Override
    public TileRemoverService getTileRemoverService() {
        return getService().getTileRemoverService();
    }

    @Override
    public SensorServiceInterface getSensorService() {
        return getService().getSensorService();
    }


    @Override
    public void startForeground(int id, Notification notification) {
        getService().startForeground(id, notification);
    }

    @Override
    public void stopForeground(boolean b) {
        getService().stopForeground(b);
    }

    @Override
    public void appendStatusText(StringBuilder content) {
        getService().appendStatusText(content);
    }

}
