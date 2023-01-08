package ch.bailu.aat_gtk.dispatcher

import ch.bailu.aat_gtk.view.toplevel.InfoCache
import ch.bailu.aat_lib.dispatcher.ContentSource
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID

class SelectedSource(val cache: InfoCache = InfoCache()) : ContentSource() {
    var selectedIID = InfoID.TRACKER

    fun requestUpdate(iid: Int) {
        if (selectedIID == iid) {
            requestUpdate()
        }
    }

    override fun requestUpdate() {
        if (cache.isInCache(selectedIID)) {
            sendUpdate(selectedIID, cache.get(selectedIID))
        }
    }

    override fun onPause() {}

    override fun onResume() {}

    override fun getIID(): Int {
        return selectedIID
    }

    override fun getInfo(): GpxInformation {
        return cache.get(selectedIID)
    }

    fun selectIndexAndUpdate(index: Int) {
        cache.withKeyAt(index) {
            if (it != selectedIID) {
                selectedIID = it
                requestUpdate()
            }
        }
    }
}
