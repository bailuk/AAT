package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode

class DistanceWalker(private val segment: Segment) : GpxListWalker() {
    private var index = 0
    var distanceDelta: Float = 0f
        private set
    var distanceOffset: Float = 0f
        private set

    override fun doList(track: GpxList): Boolean {
        if (segment.isValid()) {
            return true
        } else {
            this.distanceDelta = track.getDelta().getDistance()
            return false
        }
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        return doDelta(segment.segmentSize, segment.getDistance())
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return doDelta(marker.segmentSize, marker.getDistance())
    }

    private fun doDelta(size: Int, distance: Float): Boolean {
        if (segment.isAfter(index)) {
            return false
        } else {
            val nextIndex = index + size

            if (segment.isBefore(nextIndex)) {
                index = nextIndex
                this.distanceOffset += distance
                return false
            }
        }
        return true
    }

    override fun doPoint(point: GpxPointNode) {
        if (segment.isBefore(index)) {
            this.distanceOffset += point.getDistance()
        } else if (!segment.isAfter(index)) {
            this.distanceDelta += point.getDistance()
        }
        index++
    }
}
