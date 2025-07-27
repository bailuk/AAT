package ch.bailu.aat_lib.map.layer.gpx.painter

import ch.bailu.aat_lib.lib.color.AltitudeColorTable
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.TwoNodes
import ch.bailu.aat_lib.service.elevation.ElevationProvider

class RoutePainterNode(private val mcontext: MapContext, private val color: Int, minPixelSpace: Int)
    : GpxListPainter(mcontext, minPixelSpace) {
    override fun drawEdge(nodes: TwoNodes) {}
    override fun drawNode(node: TwoNodes.PixelNode) {
        val c: Int
        val altitude = node.point.getAltitude()
        c =
            if (altitude == ElevationProvider.NULL_ALTITUDE) color else AltitudeColorTable.instance()
                .getColor(altitude.toInt())
        mcontext.draw().bitmap(mcontext.draw().getNodeBitmap(), node.pixel, c)
    }
}
