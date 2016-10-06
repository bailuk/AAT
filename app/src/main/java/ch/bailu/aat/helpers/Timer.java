package ch.bailu.aat.helpers;

import android.os.Handler;

import java.io.Closeable;

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
        handler.postDelayed(listener, interval);
    }

    public void close () {
        if (handler != null) {
            handler.removeCallbacks(listener);
            handler=null;
        }
    }
}

