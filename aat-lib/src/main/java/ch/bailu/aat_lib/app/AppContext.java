package ch.bailu.aat_lib.app;

import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public interface AppContext {
    Broadcaster getBroadcaster();

    ServicesInterface getServices();

    StorageInterface getStorage();

    FocFactory getFocFactory();
}
