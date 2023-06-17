package ch.bailu.aat.util;

import android.util.Log;

import ch.bailu.aat_lib.logger.Logger;
import ch.bailu.aat_lib.logger.LoggerFactory;

public class AndroidLoggerFactory implements LoggerFactory {
    @Override
    public Logger warn() {
        return Log::w;
    }

    @Override
    public Logger info() {
        return Log::i;
    }

    @Override
    public Logger debug() {
        return Log::d;
    }

    @Override
    public Logger error() {
        return Log::e;
    }
}
