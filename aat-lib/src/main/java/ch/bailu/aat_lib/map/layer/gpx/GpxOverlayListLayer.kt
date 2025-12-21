package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.overlay.SolidCustomOverlayList
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Point

class GpxOverlayListLayer(
    storage: StorageInterface,
    mcontext: MapContext,
    services: ServicesInterface,
    d: DispatcherInterface
) : MapLayerInterface {
    private val overlays = ArrayList<GpxDynLayer>(SolidCustomOverlayList.MAX_OVERLAYS).apply {
        for (i in 0 until SolidCustomOverlayList.MAX_OVERLAYS) {
            add(GpxDynLayer(storage, mcontext, services, d, InfoID.OVERLAY + i))
        }
    }


    override fun drawForeground(mcontext: MapContext) {}
    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun drawInside(mcontext: MapContext) {
        for (o in overlays) o.drawInside(mcontext)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        for (o in overlays) o.onPreferencesChanged(storage, key)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun onAttached() {}
    override fun onDetached() {}
}
