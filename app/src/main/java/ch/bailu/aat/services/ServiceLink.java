package ch.bailu.aat.services;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.io.Closeable;

import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.dem.ElevationService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.services.location.LocationService;
import ch.bailu.aat.services.render.RenderService;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.services.tracker.TrackerService;

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

    private void releaseService() {
        if (service != null) {
            service.free(ServiceLink.class.getSimpleName());
            service = null;
        }
    }


    private void bindService() {
        if (bound == false) {
            bound = true;
            context.bindService(new Intent(context,
                    OneService.class), this, Context.BIND_AUTO_CREATE);
        }
    }


    private void unbindService() {
        if (bound) {
            bound = false;
            context.unbindService(this);
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
    public boolean lock() {
        return isUp() && getService().lock();
    }

    @Override
    public void free() {
        if (isUp()) getService().free();
    }


    @Override
    public void lock(String s) {
        if (isUp()) getService().lock(s);
    }


    @Override
    public void free(String s) {
        if (isUp()) getService().free(s);
    }



    @Override
    public void close() {
        down();
    }


    @Override
    public TrackerService getTrackerService() {
        return getService().getTrackerService();
    }

    @Override
    public LocationService getLocationService() {return getService().getLocationService(); }

    @Override
    public BackgroundService getBackgroundService() {
        return getService().getBackgroundService();
    }

    @Override
    public CacheService getCacheService() {
        return getService().getCacheService();
    }

    @Override
    public ElevationService getElevationService() {
        return getService().getElevationService();
    }

    @Override
    public IconMapService getIconMapService() {
        return getService().getIconMapService();
    }

    @Override
    public DirectoryService getDirectoryService() {
        return getService().getDirectoryService();
    }

    @Override
    public RenderService getRenderService() { return getService().getRenderService();}


    @Override
    public TileRemoverService getTileRemoverService() {
        return getService().getTileRemoverService();
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
