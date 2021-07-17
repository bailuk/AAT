package ch.bailu.aat_lib.logger;

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
    public void w(String tag, String msg) {
        console.w(tag, msg);
        broadcaster.broadcast(id, tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        console.i(tag, msg);
        broadcaster.broadcast(id, tag, msg);
    }

    @Override
    public void d(String tag, String msg) {
        console.d(tag, msg);
        broadcaster.broadcast(id, tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        console.e(tag, msg);
        broadcaster.broadcast(id, tag, msg);
    }
}
