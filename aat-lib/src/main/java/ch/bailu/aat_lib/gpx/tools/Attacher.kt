package ch.bailu.aat_lib.gpx.tools

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode

class Attacher(val newList: GpxList) : GpxListWalker() {
    private var newSegment = true

    override fun doList(toAttach: GpxList): Boolean {
        newSegment = true
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
            newList.appendToNewSegment(point.point, point.getAttributes())
            newSegment = false
        } else {
            newList.appendToCurrentSegment(point.point, point.getAttributes())
        }
    }
}
