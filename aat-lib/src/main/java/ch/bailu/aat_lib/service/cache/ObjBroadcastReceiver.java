package ch.bailu.aat_lib.service.cache;

import ch.bailu.aat_lib.app.AppContext;

public interface ObjBroadcastReceiver {
    void onDownloaded(String id, String url, AppContext appContext);
    void onChanged(String id, AppContext appContext);
}
