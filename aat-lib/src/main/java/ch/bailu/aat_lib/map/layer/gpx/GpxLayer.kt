package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.map.MapColor.getColorFromIID
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point

abstract class GpxLayer : MapLayerInterface, TargetInterface {
    var colorFromIID = 0
        private set

    var gpxList = GpxList.NULL_ROUTE
        private set

    override fun drawForeground(mcontext: MapContext) {}
    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        gpxList = info.getGpxList() ?: GpxList.NULL_ROUTE
        colorFromIID = getColorFromIID(iid)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun onAttached() {}
    override fun onDetached() {}
    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
}
