package ch.bailu.aat_lib.logger;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.dispatcher.Broadcaster;

public class BroadcastLogger implements Logger {

    private final Logger console;
    private final String id;
    private final Broadcaster broadcaster;

    public BroadcastLogger(Broadcaster broadcaster, String id, Logger console) {
        this.console = console;
        this.broadcaster = broadcaster;
        this.id = id;
    }

    @Override
    public void log(@Nonnull String tag, @Nonnull String msg) {
        console.log(tag, msg);
        broadcaster.broadcast(id, tag, msg);
    }
}
