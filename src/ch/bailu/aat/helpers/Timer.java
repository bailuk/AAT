package ch.bailu.aat.helpers;

import java.io.Closeable;

import android.os.Handler;

public class Timer implements Closeable {
    private Handler timer;
    private final Runnable listener;
    private final long interval;


    public Timer(Runnable l, long i) {
        listener=l;
        interval=i;
    }

    public void kick() {
        if (timer == null) {
            timer = new Handler();
        }

        if (timer != null) {
            timer.postDelayed(listener, interval);
        }
    }

    public void close () {
        if (timer != null) {
            timer.removeCallbacks(listener);
            timer=null;
        }
    }
}

