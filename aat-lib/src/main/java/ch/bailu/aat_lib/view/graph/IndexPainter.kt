package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.map.MapColor

class IndexPainter(
    private val plotter: GraphPlotter,
    private val nodeIndex: Int,
    private val offset: Float
) : GpxListWalker() {
    private var distance = 0f
    private var index = 0

    override fun doList(track: GpxList): Boolean {
        return true
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        return doDelta(segment)
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return doDelta(marker)
    }


    private fun doDelta(segment: GpxSegmentNode): Boolean {
        if (index + segment.segmentSize <= nodeIndex) {
            index += segment.segmentSize
            distance += segment.getDistance()
            return false
        }
        return true
    }

    override fun doPoint(point: GpxPointNode) {
        if (index == nodeIndex) {
            distance += point.getDistance()
            plotPoint(point, distance - offset)
            index++
        } else if (index < nodeIndex) {
            distance += point.getDistance()
            index++
        }
    }

    private fun plotPoint(point: GpxPointNode, distance: Float) {
        plotter.plotPoint(
            distance, point.getAltitude(),
            MapColor.NODE_SELECTED
        )
    }
}
