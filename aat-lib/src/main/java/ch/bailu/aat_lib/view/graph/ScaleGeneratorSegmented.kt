package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode

class ScaleGeneratorSegmented(p: GraphPlotter, private val segment: Segment) : ScaleGenerator(p) {
    private var index = 0

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return doDelta(marker.segmentSize)
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        return doDelta(segment.segmentSize)
    }

    private fun doDelta(size: Int): Boolean {
        if (segment.isAfter(index)) {
            return false
        } else {
            val nextIndex = index + size

            if (segment.isBefore(nextIndex)) {
                index = nextIndex
                return false
            }
        }
        return true
    }

    override fun doPoint(point: GpxPointNode) {
        if (segment.isInside(index)) {
            super.doPoint(point)
        }
        index++
    }
}
