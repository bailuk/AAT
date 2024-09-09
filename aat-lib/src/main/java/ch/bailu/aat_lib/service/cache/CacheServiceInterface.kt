package ch.bailu.aat_lib.service.cache;

import ch.bailu.aat_lib.util.WithStatusText;

public interface CacheServiceInterface extends WithStatusText {
    void onLowMemory();

    void close();

    Obj getObject(String path, Obj.Factory factory);

    void addToBroadcaster(ObjBroadcastReceiver obj);

    Obj getObject(String id);
}
