package ch.bailu.aat_lib.dispatcher;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface BroadcastReceiver {
    void onReceive(@Nonnull String ...args);
}
