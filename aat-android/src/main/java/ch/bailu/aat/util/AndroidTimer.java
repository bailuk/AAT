package ch.bailu.aat.util;

import android.os.Handler;
import android.os.Looper;

import ch.bailu.aat_lib.util.Timer;

public class AndroidTimer implements Timer {
    private final Handler handler;
    private Runnable run = null;

    public AndroidTimer() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        handler = new Handler();
    }


    @Override
    public void kick(Runnable run, long interval) {
        cancel();
        handler.postDelayed(run, interval);
        this.run = run;
    }

    @Override
    public void cancel() {
        if (run != null) {
            handler.removeCallbacks(run);
            run = null;
        }
    }
}
