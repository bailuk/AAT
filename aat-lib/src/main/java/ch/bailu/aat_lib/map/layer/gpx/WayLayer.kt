package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.gpx.painter.WayPainter
import ch.bailu.aat_lib.service.ServicesInterface

class WayLayer(mcontext: MapContext, private val services: ServicesInterface) :
    GpxLayer() {
    companion object {
        private const val MAX_VISIBLE_NODES = 100
        private const val ICON_SIZE = 20
    }

    private val iconSize: Int = mcontext.getMetrics().getDensity().toPixelInt(ICON_SIZE.toFloat())

    override fun drawInside(mcontext: MapContext) {
        WayPainter(mcontext, services, colorFromIID, iconSize, MAX_VISIBLE_NODES).walkTrack(gpxList)
    }
}
