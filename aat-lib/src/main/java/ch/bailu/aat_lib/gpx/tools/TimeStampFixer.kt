package ch.bailu.aat_lib.gpx.tools

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.GpxPointFirstNode
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes

class TimeStampFixer : GpxListWalker() {
    var newList: GpxList? = null
        private set
    private var newSegment = true
    private var previousTimeStamp: Long = 0
    override fun doList(track: GpxList): Boolean {
        newList = GpxList(track.getDelta().getType(), GpxListAttributes.NULL)
        return true
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        previousTimeStamp = 0
        newSegment = true
        return true
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return true
    }

    override fun doPoint(point: GpxPointNode) {
        if (hasNoErrors(point)) {
            if (newSegment) {
                newSegment = false
                newList?.appendToNewSegment(GpxPoint(point), point.getAttributes())
            } else {
                newList?.appendToCurrentSegment(GpxPoint(point), point.getAttributes())
            }
            previousTimeStamp = point.getTimeStamp()
        }
    }

    private fun hasNoErrors(point: GpxPointNode): Boolean {
        return if (timeMoreThanPrevious(point)) {
            if (isLastInSegment(point)) {
                timeNoSkip(point, FIVE_MINUTES)
            } else {
                timeLessThanNext(point, point.next as GpxPointNode?)
            }
        } else false
    }

    private fun timeMoreThanPrevious(point: GpxPointNode): Boolean {
        return previousTimeStamp < point.getTimeStamp()
    }

    private fun timeLessThanNext(point: GpxPointNode, next: GpxPointNode?): Boolean {
        return point.getTimeStamp() < next!!.getTimeStamp()
    }

    private fun timeNoSkip(point: GpxPointNode, time: Long): Boolean {
        return previousTimeStamp + time > point.getTimeStamp()
    }

    private fun isLastInSegment(point: GpxPointNode): Boolean {
        return point.next == null || point.next is GpxPointFirstNode
    }

    companion object {
        private const val FIVE_MINUTES = (1000 * 60 * 5).toLong()
    }
}
