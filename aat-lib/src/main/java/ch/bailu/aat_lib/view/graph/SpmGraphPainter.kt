package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode

class SpmGraphPainter(private val entries: Array<SpmEntry>, minDistance: Int) : GpxListWalker() {
    private var distance = 0f
    private val minDistance: Float = minDistance.toFloat()

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return true
    }


    override fun doPoint(point: GpxPointNode) {
        for (e in entries) {
            distance += point.getDistance()

            e.incrementSummaryDistance(point.getDistance())
            e.plotIfDistance(point, minDistance, distance)
        }
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        return true
    }

    override fun doList(track: GpxList): Boolean {
        return true
    }
}
