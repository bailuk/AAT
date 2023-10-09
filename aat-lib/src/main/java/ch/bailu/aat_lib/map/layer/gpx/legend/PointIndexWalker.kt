package ch.bailu.aat_lib.map.layer.gpx.legend

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode

class PointIndexWalker : LegendWalker() {
    private var index = 1
    override fun doList(track: GpxList): Boolean {
        index = 1
        return super.doList(track)
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return legendContext!!.isVisible(marker.getBoundingBox())
    }

    override fun doPoint(point: GpxPointNode) {
        legendContext!!.nodes.nodeB.set(point)
        if (!legendContext!!.arePointsTooClose()) {
            drawLegendFromB()
            legendContext!!.nodes.switchNodes()
        }
        index++
    }

    private fun drawLegendFromB() {
        if (legendContext!!.isBVisible) {
            legendContext!!.drawLabelB(index.toString())
        }
    }
}
