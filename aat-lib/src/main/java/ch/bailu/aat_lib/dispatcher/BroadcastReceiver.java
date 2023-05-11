package ch.bailu.aat_lib.dispatcher;

@FunctionalInterface
public interface BroadcastReceiver {
    void onReceive(String ...args);
}
