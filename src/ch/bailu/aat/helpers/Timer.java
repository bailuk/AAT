package ch.bailu.aat.helpers;

import java.io.Closeable;

import android.os.Handler;

public class Timer implements Closeable {
    private Handler handler;
    private final Runnable listener;
    private final long interval;


    public Timer(Runnable l, long i) {
        listener=l;
        interval=i;
    }

    public void kick() {
        if (handler == null) {
            handler = new Handler();
        }

        if (handler != null) {
            handler.postDelayed(listener, interval);
        }
    }

    public void close () {
        if (handler != null) {
            handler.removeCallbacks(listener);
            handler=null;
        }
    }
}

