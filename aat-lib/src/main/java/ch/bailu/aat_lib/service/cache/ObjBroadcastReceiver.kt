package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext

interface ObjBroadcastReceiver {
    fun onDownloaded(id: String, url: String, appContext: AppContext)
    fun onChanged(id: String, appContext: AppContext)
}
