package ch.bailu.aat.services;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ch.bailu.aat.util.AndroidTimer;
import ch.bailu.aat_lib.logger.AppLog;

public abstract class AbsService  extends Service {
    private static int allinstances, allcreations;
    private int creations;


    private long startTime;

    private boolean up = false;
    private int lock = 0;



    public synchronized boolean lock() {
        if (up) {
            lock++;

            try {
                startService(new Intent(this, OneService.class));
            } catch (IllegalStateException e) {
                AppLog.w(this, e);
            }

            lazyOff.cancel();

        }
        return up;
    }



    public synchronized void free() {
        if (up) {
            lock--;

            if (lock == 0) {
                lazyOff.kick(15*1000, this::stopService);

            } else if (lock < 0) {
                AppLog.w(this, "lock < 0 !!!");
            }
        }
    }



    private final Set<String> locks = new HashSet<>();

    private final AndroidTimer lazyOff = new AndroidTimer();


    private synchronized void stopService() {

        if (lock == 0) {
            lazyOff.cancel();
            stopSelf();
        } else if (lock < 0) {
            AppLog.w(this, "lock < 0 !!!");
        }
    }

    public synchronized void lock(String r) {
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
            return AbsService.this;
        }
    }


    @Override
    public synchronized void onCreate() {
        super.onCreate();



        up = true;
        allcreations++;
        creations++;


        startTime=System.currentTimeMillis();
    }


    protected void onDestroyCalled() {
        up = false;
    }

    @Override
    public synchronized void onDestroy() {

        creations--;
        allcreations--;

        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return
                new CommonBinder();
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
