package ch.bailu.aat.util;

import android.os.Handler;
import android.os.Looper;

import java.io.Closeable;

public class Timer implements Closeable {
    private final Handler handler;
    private final Runnable listener;
    private final long defaultInterval;


    public Timer(Runnable l, long i) {
        listener=l;
        defaultInterval =i;

        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        handler = new Handler();
    }

    public void kick() {
        kick(defaultInterval);
    }

    public void kick(long interval) {
        cancel();
        handler.postDelayed(listener, interval);
    }

    public void close () {
        cancel();
    }
    public void cancel() {
        handler.removeCallbacks(listener);
    }



}

