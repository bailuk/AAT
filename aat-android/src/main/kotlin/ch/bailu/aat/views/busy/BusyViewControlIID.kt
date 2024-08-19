package ch.bailu.aat.views.busy

import android.view.ViewGroup
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation

open class BusyViewControlIID(parent: ViewGroup) : BusyViewControl(parent), OnContentUpdatedInterface {
    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (info.getLoaded()) stopWaiting(iid) else startWaiting(iid)
    }
}
