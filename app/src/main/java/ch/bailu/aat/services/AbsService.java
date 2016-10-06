package ch.bailu.aat.services;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.Timer;

public abstract class AbsService  extends Service {
    private static int allinstances, allcreations;
    private int creations;


    private long startTime;



    private final Set<String> locks = new HashSet<>();

    private final Timer lazyOff = new Timer(new Runnable() {
        @Override
        public void run() {
            if (locks.isEmpty()) {
                AppLog.d(this, "turn off.");
                stopSelf();
            }
        }

    }, 15*1000); 


    public void lock(String r) {
        if (locks.add(r)) {
            startService(new Intent(this,  OneService.class));
            lazyOff.close();
        }
        
        AppLog.d(this, "locked " + locks.size() + " times.");
    }


    public void free(String r) {
        if (locks.remove(r) && locks.isEmpty()) {
            lazyOff.kick();
        }
    }
    

    public AbsService() {
        allinstances++;
    }


    @Override
    protected void finalize() throws Throwable {
        allinstances--;
        super.finalize();
    }





    public class CommonBinder extends Binder {
        public AbsService getService() {
            return AbsService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();

        AppLog.d(this, "onCreate()");
        allcreations++;
        creations++;

        startTime=System.currentTimeMillis();
    }


    @Override
    public void onDestroy() {

        creations--;
        allcreations--;

        AppLog.d(this, "onDestroy()");
        super.onDestroy();
    }    


    @Override
    public IBinder onBind(Intent intent) {
        return new CommonBinder();
    }


    @Override
    public boolean onUnbind(Intent intent) {
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
