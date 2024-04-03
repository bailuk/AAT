package ch.bailu.aat_lib.map.layer.gpx.painter

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.TwoNodes
import org.mapsforge.core.graphics.Paint

class EdgePainter(private val mcontext: MapContext, private val paint: Paint, minPixelSpace: Int)
    : GpxListPainter(mcontext, minPixelSpace = minPixelSpace) {
    override fun drawEdge(nodes: TwoNodes) {
        mcontext.draw().edge(nodes, paint)
    }

    override fun drawNode(node: TwoNodes.PixelNode) {}
}