package ch.bailu.aat.services;


import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;

public abstract class AbsService  extends Service {
    private static int allbindings, allinstances, allcreations;
    private int bindings, creations;
    
    
    private long startTime;
    private MultiServiceLink serviceLink=MultiServiceLink.NULL_SERVICE_LINK;

    
    public AbsService() {
        allinstances++;
    }
    
    
    @Override
    protected void finalize() throws Throwable {
        allinstances--;
        super.finalize();
    }
    
    
    public void connectToServices(Class<?>[] services) {
        serviceLink=new MultiServiceLink(this,services) {

            @Override
            public void onServicesUp() {
                AbsService.this.onServicesUp();
            }
        };
    }

    public abstract void onServicesUp();




    public AbsService getService(Class<?> s) throws ServiceNotUpException {
        return serviceLink.getService(s);
    }


    public CacheService getCacheService() throws ServiceNotUpException {
        return (CacheService) getService(CacheService.class);
    }


    public BackgroundService getBackgroundService() throws ServiceNotUpException {
        return (BackgroundService) getService(BackgroundService.class);
    }



    public class CommonBinder extends Binder {
        public AbsService getService() {
            return AbsService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //AppLog.d(this, "onCreate()");
        allcreations++;
        creations++;
        
        startTime=System.currentTimeMillis();
    }

    
    @Override
    public void onDestroy() {
        //AppLog.d(this, "onDestroy()");

        
        serviceLink.cleanUp();
        serviceLink=null;
        
        creations--;
        allcreations--;
        super.onDestroy();
    }    
    

    @Override
    public IBinder onBind(Intent intent) {
        allbindings++;
        bindings++;
        
        return new CommonBinder();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        allbindings--;
        bindings--;
        
        return false;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    public void appendStatusText(StringBuilder builder) {
        builder.append("<h1>");
        builder.append(getClass().getSimpleName());
        builder.append("</h1>");

        builder.append("<p>Start time: ");
        builder.append(formatDate(startTime));
        builder.append(" - ");
        builder.append(formatTime(startTime));
        builder.append("<br>Created services: ");
        builder.append(creations);
        builder.append("<br>Service bindings: ");
        builder.append(bindings);
        
        builder.append("</p>");
    }


    public static void appendStatusTextStatic(StringBuilder builder) {
        builder.append("<h1>");
        builder.append(AbsService.class.getSimpleName());
        builder.append("</h1>");

        builder.append("<p>Instantiated services: ");
        builder.append(allinstances);
        builder.append("<br>Created services: ");
        builder.append(allcreations);
        builder.append("<br>Service bindings: ");
        builder.append(allbindings);
        builder.append("</p>");
    }

    
    private String formatDate(long time) {
        Date date = new Date(time);
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        return dateFormat.format(date);
    }

    private String formatTime(long time) {
        Date date = new Date(time);
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());
        return dateFormat.format(date);
    }

}
