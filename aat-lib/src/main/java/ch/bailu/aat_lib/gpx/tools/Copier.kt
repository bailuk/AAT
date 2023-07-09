package ch.bailu.aat_lib.gpx.tools

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes

class Copier : GpxListWalker() {
    var newList: GpxList? = null
        private set

    private var newSegment = true
    override fun doList(track: GpxList): Boolean {
        newList = GpxList(
            track.getDelta().getType(),
            GpxListAttributes.NULL
        )
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
            newList?.appendToNewSegment(point.point, point.getAttributes())
            newSegment = false
        } else {
            newList?.appendToCurrentSegment(point.point, point.getAttributes())
        }
    }
}
