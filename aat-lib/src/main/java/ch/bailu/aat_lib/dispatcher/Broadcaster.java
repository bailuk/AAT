package ch.bailu.aat_lib.dispatcher;

public interface Broadcaster {


    void broadcast(String broadcastId, String ...args);

    void register(BroadcastReceiver onLocation, String locationChanged);
    void unregister(BroadcastReceiver onLocation);
}
