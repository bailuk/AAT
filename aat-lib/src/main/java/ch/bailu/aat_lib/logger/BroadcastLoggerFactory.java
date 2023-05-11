package ch.bailu.aat_lib.logger;

import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.Broadcaster;

public class BroadcastLoggerFactory implements LoggerFactory {
    private final Broadcaster broadcaster;
    private final LoggerFactory fallback;

    public BroadcastLoggerFactory(Broadcaster broadcaster, LoggerFactory fallback) {
        this.broadcaster = broadcaster;
        this.fallback = fallback;
    }

    @Override
    public Logger warn() {
        return fallback.warn();
    }

    @Override
    public Logger info() {
        return new BroadcastLogger(broadcaster, AppBroadcaster.LOG_INFO, fallback.info());
    }

    @Override
    public Logger debug() {
        return fallback.debug();
    }

    @Override
    public Logger error() {
        return new BroadcastLogger(
                broadcaster,
                AppBroadcaster.LOG_ERROR,
                fallback.error());
    }
}
