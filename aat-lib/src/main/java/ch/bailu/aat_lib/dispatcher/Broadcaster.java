package ch.bailu.aat_lib.dispatcher;

public interface Broadcaster {


    void broadcast(String broadcastId, Object ...args);

    void register(BroadcastReceiver onLocation, String locationChanged);
    void unregister(BroadcastReceiver onLocation);
}
