package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlayList
import javax.annotation.Nonnull

class OverlaysSource(context: AppContext) : ContentSourceInterface {
    private val overlays = ArrayList<OverlaySource>()

    init {
        for (i in 0 until SolidCustomOverlayList.MAX_OVERLAYS) {
            overlays.add(OverlaySource.factoryCustomOverlaySource(context, i))
        }
    }

    override fun onPause() {
        overlays.forEach {it.onPause()}
    }

    override fun onResume() {
        overlays.forEach{ it.onResume() }
    }

    override fun getIID(): Int {
        return InfoID.OVERLAY
    }

    override fun getInfo(): GpxInformation {
        return GpxInformation.NULL
    }

    override fun setTarget(@Nonnull target: OnContentUpdatedInterface) {
        overlays.forEach {it.setTarget(target)}
    }

    override fun requestUpdate() {
        overlays.forEach {it.requestUpdate()}
    }
}
