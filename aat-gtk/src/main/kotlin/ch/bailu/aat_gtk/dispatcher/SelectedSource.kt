package ch.bailu.aat_gtk.dispatcher

import ch.bailu.aat_gtk.view.toplevel.InfoCache
import ch.bailu.aat_lib.dispatcher.ContentSource
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID

class SelectedSource(val cache: InfoCache = InfoCache()) : ContentSource(), OnContentUpdatedInterface {
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
            select(it)
        }
    }

    fun select(infoID: Int) {
        if (infoID != selectedIID) {
            selectedIID = infoID
            requestUpdate()
        }
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        cache.onContentUpdated(iid, info)
        requestUpdate(iid)
    }

    fun getIntermediateDispatcher(dispatcher: Dispatcher, vararg iid: Int): Dispatcher {
        val result = Dispatcher()
        dispatcher.addTarget(this, *iid)
        result.addSource(this)
        result.onResume()
        return result
    }
}
