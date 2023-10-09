package ch.bailu.aat_lib.map.layer.gpx.legend

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode

class SegmentIndexWalker : LegendWalker() {
    private var index = 1
    override fun doList(track: GpxList): Boolean {
        index = 1
        return super.doList(track)
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        legendContext!!.nodes.nodeB.set((segment.firstNode as GpxPointNode))
        if (!legendContext!!.arePointsTooClose() || index == 1) {
            drawLegendFromB()
            legendContext!!.nodes.switchNodes()
        }
        index++
        return segment.next == null
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return marker.next == null
    }

    override fun doPoint(point: GpxPointNode) {
        if (point.next == null) {
            legendContext!!.nodes.nodeB.set(point)
            drawLegendFromB()
        }
    }

    private fun drawLegendFromB() {
        if (legendContext!!.isBVisible) {
            legendContext!!.drawNodeB()
            if (!legendContext!!.arePointsTooClose()) legendContext!!.drawLabelB(index.toString())
        }
    }
}
