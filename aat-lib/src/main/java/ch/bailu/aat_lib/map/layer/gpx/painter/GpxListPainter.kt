package ch.bailu.aat_lib.map.layer.gpx.painter

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.TwoNodes
import ch.bailu.aat_lib.map.TwoNodes.PixelNode
import ch.bailu.aat_lib.map.layer.gpx.DistanceCounter

abstract class GpxListPainter @JvmOverloads constructor(
    private val mcontext: MapContext,
    minPixelSpace: Int = MIN_PIXEL_SPACE
) : GpxListWalker() {
    private val edgeDistance: DistanceCounter
    private val nodeDistance: DistanceCounter
    private var action = START_PAINTING

    init {
        val res = mcontext.getMetrics().getDensity()
        edgeDistance = DistanceCounter(
            mcontext.getMetrics()
                .pixelToDistance(res.toPixelFloat(minPixelSpace.toFloat()).toInt()),
            mcontext.getMetrics()
                .pixelToDistance(res.toPixelFloat(MAX_PIXEL_SPACE.toFloat()).toInt())
        )
        nodeDistance = DistanceCounter(
            mcontext.getMetrics()
                .pixelToDistance(res.toPixelFloat(minPixelSpace.toFloat()).toInt()),
            mcontext.getMetrics()
                .pixelToDistance(res.toPixelFloat(MAX_PIXEL_SPACE.toFloat()).toInt())
        )
    }

    abstract fun drawEdge(nodes: TwoNodes)
    abstract fun drawNode(node: PixelNode)
    override fun doList(track: GpxList): Boolean {
        edgeDistance.reset()
        nodeDistance.reset()
        return mcontext.getMetrics().isVisible(track.getDelta().getBoundingBox())
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        restartPainting()
        return mcontext.getMetrics().isVisible(segment.getBoundingBox())
    }

    private fun restartPainting() {
        action = START_PAINTING
        edgeDistance.reset()
        nodeDistance.reset()
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return if (mcontext.getMetrics().isVisible(marker.getBoundingBox())) {
            if (action == START_PAINTING) {
                doFirstNode(marker)
            }
            if (marker.getDistance() < edgeDistance.min) {
                doMarkerFirstNode(marker)
                return false
            }
            true
        } else {
            restartPainting()
            false
        }
    }

    private fun doFirstNode(marker: GpxSegmentNode) {
        action = CONTINUE_PAINTING
        mcontext.getTwoNodes().nodeA.set(marker.firstNode as GpxPointNode)
        drawNode(mcontext.getTwoNodes().nodeA)
        edgeDistance.reset()
        nodeDistance.reset()
    }

    private fun doMarkerFirstNode(marker: GpxSegmentNode) {
        drawNodeIfDistance(marker.firstNode as GpxPointInterface)
        edgeDistance.add(marker.getDistance())
        nodeDistance.add(marker.getDistance())
    }

    override fun doPoint(point: GpxPointNode) {
        edgeDistance.add(point.getDistance())
        nodeDistance.add(point.getDistance())
        drawNodeIfDistance(point)
    }

    private fun drawNodeIfDistance(point: GpxPointInterface) {
        if (edgeDistance.hasDistance() && nodeDistance.isTooSmall) {
            mcontext.getTwoNodes().nodeB.set(point)
            drawEdgeIfVisible()
            edgeDistance.reset()
            mcontext.getTwoNodes().switchNodes()
        } else if (nodeDistance.hasDistance()) {
            mcontext.getTwoNodes().nodeB.set(point)
            drawEdgeIfVisible()
            drawNodeIfVisible()
            edgeDistance.reset()
            nodeDistance.reset()
            mcontext.getTwoNodes().switchNodes()
        }
    }

    private fun drawEdgeIfVisible() {
        if (mcontext.getTwoNodes().nodeB.isVisible ||
            mcontext.getTwoNodes().nodeA.isVisible ||
            edgeDistance.isTooLarge
        ) {
            drawEdge(mcontext.getTwoNodes())
        }
    }

    private fun drawNodeIfVisible() {
        if (mcontext.getTwoNodes().nodeB.isVisible) {
            drawNode(mcontext.getTwoNodes().nodeB)
        }
    }

    companion object {
        private const val MIN_PIXEL_SPACE = 10
        private const val MAX_PIXEL_SPACE = 100
        private const val START_PAINTING = 0
        private const val CONTINUE_PAINTING = 2
    }
}
