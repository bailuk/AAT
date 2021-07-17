package ch.bailu.aat_lib.dispatcher;

public interface Broadcaster {


    void broadcast(String broadcastId, Object ...objects);

    void register(BroadcastReceiver onLocation, String locationChanged);
    void unregister(BroadcastReceiver onLocation);
}
