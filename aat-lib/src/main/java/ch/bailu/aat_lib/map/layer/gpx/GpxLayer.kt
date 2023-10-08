package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.map.MapColor.getColorFromIID
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface

abstract class GpxLayer : MapLayerInterface, OnContentUpdatedInterface {
    private var color = 0

    fun getColor(): Int {
        return color
    }

    var gpxList = GpxList.NULL_ROUTE
        private set

    override fun drawForeground(mcontext: MapContext) {}
    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setTrack(info.gpxList)
        color = getColorFromIID(iid)
    }

    private fun setTrack(gpx: GpxList?) {
        gpxList = gpx ?: GpxList.NULL_ROUTE
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
}
