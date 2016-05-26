package ch.bailu.aat.services.cache;

import ch.bailu.aat.services.ServiceContext;

public interface ObjectBroadcastReceiver {
    void onDownloaded(String id, String url, ServiceContext sc);
    void onChanged(String id, ServiceContext sc);
}
