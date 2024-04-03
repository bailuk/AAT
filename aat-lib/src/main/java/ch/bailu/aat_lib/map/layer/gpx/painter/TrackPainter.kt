package ch.bailu.aat_lib.map.layer.gpx.painter

import ch.bailu.aat_lib.lib.color.AltitudeColorTable
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.TwoNodes
import org.mapsforge.core.graphics.Paint

class TrackPainter(private val mcontext: MapContext, private val paint: Paint)  : GpxListPainter(mcontext) {
    override fun drawEdge(nodes: TwoNodes) {
        mcontext.draw().edge(nodes, paint)
    }

    override fun drawNode(node: TwoNodes.PixelNode) {
        val altitude = node.point.getAltitude().toInt()
        val color = AltitudeColorTable.instance().getColor(altitude)
        paint.color = color
    }
}