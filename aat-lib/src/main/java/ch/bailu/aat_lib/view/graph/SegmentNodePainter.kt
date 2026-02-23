package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.map.MapColor

class SegmentNodePainter(private val plotter: GraphPlotter, offset: Float) : GpxListWalker() {
    private var distance: Float = 0f - offset

    override fun doList(track: GpxList): Boolean {
        return true
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        if (segment.segmentSize > 0 && distance > 0f) {
            val node = segment.firstNode as GpxPointNode
            plotPoint(node, distance + node.getDistance())
        }

        distance += segment.getDistance()
        return false
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return false
    }

    override fun doPoint(point: GpxPointNode) {}

    private fun plotPoint(point: GpxPointNode, distance: Float) {
        plotter.plotPoint(
            distance, point.getAltitude(),
            MapColor.NODE_NEUTRAL
        )
    }
}
