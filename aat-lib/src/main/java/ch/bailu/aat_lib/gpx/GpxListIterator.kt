package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.gpx.attributes.GpxAttributesNull
import ch.bailu.aat_lib.gpx.linked_list.Node
import ch.bailu.aat_lib.gpx.segmented_list.SegmentNode

/** Forward iterator over a {@link GpxList}, tracking segment boundaries and point indices. */
class GpxListIterator(private val track: GpxList) {
    private inner class PointPrimerNode : GpxPointFirstNode(GpxPoint.NULL, GpxAttributesNull.NULL) {
        override var next: Node?
            get() = track.pointList.first
            set(_) {}
    }

    private inner class SegmentPrimerNode : GpxSegmentNode(PointPrimerNode()) {
        override var next: Node?
            get() = track.segmentList.first
            set(_) {}
    }

    private var point: GpxPointNode = PointPrimerNode()
    private var segment: GpxSegmentNode = SegmentPrimerNode()

    private var inSegmentIndex = -1
    private var inTrackIndex = -1


    fun nextPoint(): Boolean {
        if (setPoint(point.next)) {
            inSegmentIndex++
            inTrackIndex++

            if (inSegmentIndex == (segment as SegmentNode).segmentSize) return nextSegment()
            return true
        }
        return false
    }

    private fun setPoint(node: Node?): Boolean {
        if (node is GpxPointNode) {
            point = node
            return true
        }
        return false
    }

    private fun nextSegment(): Boolean {
        if (setSegment(segment.next)) {
            inSegmentIndex = 0
            return true
        }
        return false
    }

    private fun setSegment(node: Node?): Boolean {
        if (node is GpxSegmentNode) {
            segment = node
            return true
        }
        return false
    }

    fun getPoint(): GpxPointNode {
        return point
    }

    val isFirstInTrack: Boolean
        get() = inTrackIndex == 0

    val isFirstInSegment: Boolean
        get() = inSegmentIndex == 0
}
