package ch.bailu.aat_lib.map

import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.util.Point
import kotlin.math.abs

class TwoNodes(private val metrics: MapMetrics) {
    @JvmField
    var nodeA = PixelNode()
    @JvmField
    var nodeB = PixelNode()

    inner class PixelNode {
        @JvmField
        val pixel = Point()
        @JvmField
        var point: GpxPointInterface = GpxPoint.NULL
        val isVisible: Boolean
            get() = metrics.isVisible(point)

        fun set(tp: GpxPointInterface) {
            point = tp
            pixel.set(metrics.toPixel(tp))
        }
    }

    fun arePointsTooClose(distance: Int): Boolean {
        return abs(nodeA.pixel.x - nodeB.pixel.x) +
                abs(nodeA.pixel.y - nodeB.pixel.y) < distance
    }

    fun switchNodes() {
        val nodeT = nodeB
        nodeB = nodeA
        nodeA = nodeT
    }
}
