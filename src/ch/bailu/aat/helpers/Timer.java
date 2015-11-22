package ch.bailu.aat.helpers;

import android.os.Handler;

public class Timer {
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

    public void cleanUp() {
        if (timer != null) {
            timer.removeCallbacks(listener);
            timer=null;
        }
    }
}

