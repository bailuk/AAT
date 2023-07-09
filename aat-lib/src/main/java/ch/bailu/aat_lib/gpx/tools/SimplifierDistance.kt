package ch.bailu.aat_lib.gpx.tools

import ch.bailu.aat_lib.gpx.GpxDeltaHelper
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.GpxPointFirstNode
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes

class SimplifierDistance : GpxListWalker() {
    var newList: GpxList? = null
        private set
    private var newSegment = true
    private var lastPoint: GpxPointNode? = null

    override fun doList(track: GpxList): Boolean {
        newList = GpxList(track.getDelta().getType(), GpxListAttributes.NULL)
        return true
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        newSegment = true
        return true
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return true
    }

    override fun doPoint(point: GpxPointNode) {
        if (newSegment) {
            newSegment = false
            newList?.appendToNewSegment(GpxPoint(point), point.getAttributes())
            lastPoint = point
        } else {
            if (isLastInSegment(point) || hasDistance(point)) {
                newList?.appendToCurrentSegment(GpxPoint(point), point.getAttributes())
                lastPoint = point
            }
        }
    }

    private fun hasDistance(point: GpxPointNode): Boolean {
        return GpxDeltaHelper.getDistance(lastPoint, point) >= MIN_DISTANCE
    }

    private fun isLastInSegment(point: GpxPointNode): Boolean {
        return point.next == null || point.next is GpxPointFirstNode
    }

    companion object {
        private const val MIN_DISTANCE = 25f
    }
}
