package ch.bailu.aat.util;

import android.util.Log;

import ch.bailu.aat_lib.logger.Logger;

public class AndroidLogger implements Logger {
    @Override
    public void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    @Override
    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        Log.e(tag, msg);
    }
}
