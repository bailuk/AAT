package ch.bailu.aat.services;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ch.bailu.aat.util.Timer;
import ch.bailu.aat.util.ui.AppLog;

public abstract class AbsService  extends Service {
    private static int allinstances, allcreations;
    private int creations;


    private long startTime;

    private boolean up = false;
    private int lock = 0;



    public synchronized boolean lock() {
        if (up) {
            lock++;


            if (lock == 1) {
                AppLog.d(this, "lock ++: " + lock);
                AppLog.d(this, "=> startService()");

                startService(new Intent(this,  OneService.class));
                lazyOff.close();
            }
        }
        return up;
    }



    public synchronized void free() {
        if (up) {
            lock--;


            if (lock == 0) {
                AppLog.d(this, "lock --: " + lock);
                AppLog.d(this, "=> lazyOff.kick()");
                lazyOff.kick();

            } else if (lock < 0) {
                AppLog.d(this, "lock < 0 !!!");
            }
        }
    }



    private final Set<String> locks = new HashSet<>();

    private final Timer lazyOff = new Timer(new Runnable() {
        @Override
        public void run() {
            stopService();
        }

    }, 15*1000); 


    private synchronized void stopService() {
        AppLog.d(this, "stopService() lock: " + lock);

        if (lock == 0) {
            up = false;
            lazyOff.cancel();
            AppLog.d(this, "=> stopSelf()");
            stopSelf();
        } else if (lock < 0) {
            AppLog.d(this, "lock < 0 !!!");
        }
    }

    public synchronized void lock(String r) {
        AppLog.d(this, "lock: " + r);


        if (locks.add(r)) {
            lock();
        }
    }

    public synchronized void free(String r) {
        if (locks.remove(r)) free();
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
            AppLog.d(this, "getService()");
            return AbsService.this;
        }
    }


    @Override
    public synchronized void onCreate() {
        super.onCreate();



        up = true;
        allcreations++;
        creations++;

        AppLog.d(this, "onCreate() " + creations);

        startTime=System.currentTimeMillis();
    }


    @Override
    public synchronized void onDestroy() {

        up = false;
        creations--;
        allcreations--;

        AppLog.d(this, "onDestroy() " + creations);
        super.onDestroy();
    }    


    @Override
    public IBinder onBind(Intent intent) {
        AppLog.d(this, "onBind()");
        return
                new CommonBinder();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        AppLog.d(this, "onUnbind()");
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
