package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.TwoNodes
import ch.bailu.aat_lib.map.TwoNodes.PixelNode
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.graphics.Bitmap

class WayLayer(private val mcontext: MapContext, private val services: ServicesInterface) :
    GpxLayer() {
    private val iconSize: Int = mcontext.getMetrics().getDensity().toPixelInt(ICON_SIZE.toFloat())

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun drawInside(mcontext: MapContext) {
        WayPainter().walkTrack(gpxList)
    }

    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun onAttached() {}
    override fun onDetached() {}
    private inner class WayPainter : GpxListPainter(mcontext) {
        private var count = 0
        override fun drawEdge(nodes: TwoNodes) {}
        override fun drawNode(node: PixelNode) {
            if (node.isVisible && count < MAX_VISIBLE_NODES) {
                val nodeDrawable = getNodeIconSVG(node)

                if (nodeDrawable is Bitmap) {
                    mcontext.draw().bitmap(nodeDrawable, node.pixel)
                } else {
                    mcontext.draw().bitmap(mcontext.draw().getNodeBitmap(), node.pixel, getColor())
                }
                count++
            }
        }

        private fun getNodeIconSVG(node: PixelNode): Bitmap? {
            var result: Bitmap? = null
            services.insideContext {
                val i = services.iconMapService.getIconSVG(
                    node.point,
                    iconSize
                )
                result = i?.bitmap
            }
            return result
        }
    }

    companion object {
        private const val MAX_VISIBLE_NODES = 100
        private const val ICON_SIZE = 20
    }
}
