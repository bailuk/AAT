package ch.bailu.aat.services.cache;

import ch.bailu.aat.services.cache.CacheService.SelfOn;

public interface ObjectBroadcastReceiver {
    void onDownloaded(String id, String url, SelfOn self);
    void onChanged(String id, SelfOn self);
}
