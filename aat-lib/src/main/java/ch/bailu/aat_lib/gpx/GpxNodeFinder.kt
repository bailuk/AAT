package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.coordinates.BoundingBoxE6.Companion.doOverlap

/** Walks a {@link GpxList} to find the first point that falls within a given bounding box. */
class GpxNodeFinder(private val bounding: BoundingBoxE6) : GpxListWalker() {
    var node: GpxPointNode? = null
        private set
    var nodeIndex: Int = 0
        private set

    override fun doList(list: GpxList): Boolean {
        return doOverlap(list.getDelta().getBoundingBox(), bounding)
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        if (haveNode()) {
            return false
        } else if (doOverlap(segment.getBoundingBox(), bounding)) {
            return true
        } else {
            this.nodeIndex = this.nodeIndex + segment.segmentSize
            return false
        }
    }

    override fun doMarker(segment: GpxSegmentNode): Boolean {
        return doSegment(segment)
    }

    override fun doPoint(point: GpxPointNode) {
        if (!haveNode()) {
            if (bounding.contains(point)) {
                node = point
            } else {
                this.nodeIndex++
            }
        }
    }

    fun haveNode(): Boolean {
        return node != null
    }
}
