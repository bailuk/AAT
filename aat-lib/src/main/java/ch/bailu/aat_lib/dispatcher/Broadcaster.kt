package ch.bailu.aat_lib.dispatcher;

public interface Broadcaster {
    void broadcast(String action, String ...args);

    void register(BroadcastReceiver broadcastReceiver, String action);
    void unregister(BroadcastReceiver broadcastReceiver);
}
