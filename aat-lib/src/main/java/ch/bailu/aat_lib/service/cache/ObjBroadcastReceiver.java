package ch.bailu.aat_lib.service.cache;

import ch.bailu.aat_lib.service.ServicesInterface;

public interface ObjBroadcastReceiver {
    void onDownloaded(String id, String url, ServicesInterface sc);
    void onChanged(String id, ServicesInterface sc);
}
