package ch.bailu.aat_lib.map.layer.gpx.legend

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.map.MapColor
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.TwoNodes
import ch.bailu.aat_lib.map.TwoNodes.PixelNode
import org.mapsforge.core.graphics.Paint

class LegendContext(
    private val mcontext: MapContext,
    private val backgroundPaint: Paint,
    private val framePaint: Paint
) {
    private val minPixelDistance: Int = mcontext.getMetrics().getDensity().toPixelInt(MIN_DI_PIXEL_DISTANCE.toFloat())
    val nodes: TwoNodes = mcontext.getTwoNodes()

    fun isVisible(bounding: BoundingBoxE6?): Boolean {
        return mcontext.getMetrics().isVisible(bounding!!)
    }

    fun drawNodeB() {
        drawNode(nodes.nodeB)
    }

    fun drawNodeA() {
        drawNode(nodes.nodeA)
    }

    private fun drawNode(node: PixelNode) {
        mcontext.draw().bitmap(mcontext.draw().getNodeBitmap(), node.pixel, MapColor.NODE_NEUTRAL)
    }

    fun setA(point: GpxPointNode?) {
        nodes.nodeA.set(point!!)
    }

    fun setB(point: GpxPointNode?) {
        nodes.nodeB.set(point!!)
    }

    fun drawLabelB(text: String) {
        drawLabel(nodes.nodeB, text)
    }

    private fun drawLabel(node: PixelNode, text: String) {
        mcontext.draw().label(text, node.pixel, backgroundPaint, framePaint)
    }

    fun arePointsTooClose(): Boolean {
        return nodes.arePointsTooClose(minPixelDistance)
    }

    fun switchNodes() {
        nodes.switchNodes()
    }

    val isAVisible: Boolean
        get() = isVisible(nodes.nodeA)
    val isBVisible: Boolean
        get() = isVisible(nodes.nodeB)

    private fun isVisible(node: PixelNode): Boolean {
        return mcontext.getMetrics().isVisible(node.point)
    }

    companion object {
        const val MIN_DI_PIXEL_DISTANCE = 100
    }
}
