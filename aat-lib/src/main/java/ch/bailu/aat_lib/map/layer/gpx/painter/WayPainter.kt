package ch.bailu.aat_lib.map.layer.gpx.painter

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.TwoNodes
import ch.bailu.aat_lib.service.ServicesInterface
import org.mapsforge.core.graphics.Bitmap

class WayPainter(private val mcontext: MapContext,
                 private val services: ServicesInterface,
                 private val color: Int,
                 private val iconSize: Int,
                 private val maxNodes: Int) : GpxListPainter(mcontext) {

                     private var count = 0

    override fun drawEdge(nodes: TwoNodes) {}
    override fun drawNode(node: TwoNodes.PixelNode) {
        if (node.isVisible && count < maxNodes) {
            val nodeDrawable = getNodeIconSVG(node)

            if (nodeDrawable is Bitmap) {
                mcontext.draw().bitmap(nodeDrawable, node.pixel)
            } else {
                mcontext.draw().bitmap(mcontext.draw().getNodeBitmap(), node.pixel, color)
            }
            count++
        }
    }

    private fun getNodeIconSVG(node: TwoNodes.PixelNode): Bitmap? {
        var result: Bitmap? = null
        services.insideContext {
            val i = services.getIconMapService().getIconSVG(
                node.point,
                iconSize
            )
            result = i?.bitmap
        }
        return result
    }
}
