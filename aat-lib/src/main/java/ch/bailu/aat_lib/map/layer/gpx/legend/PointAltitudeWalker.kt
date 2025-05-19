package ch.bailu.aat_lib.map.layer.gpx.legend

import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.preferences.StorageInterface

class PointAltitudeWalker(storage: StorageInterface?) : LegendWalker() {
    private val description: DistanceDescription = DistanceDescription(storage!!)

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return legendContext!!.isVisible(marker.getBoundingBox())
    }

    override fun doPoint(point: GpxPointNode) {
        legendContext!!.setB(point)
        if (!legendContext!!.arePointsTooClose()) {
            drawLegendFromB()
            legendContext!!.switchNodes()
        }
    }

    private fun drawLegendFromB() {
        if (legendContext!!.isBVisible) {
            legendContext!!.drawLabelB(description.getAltitudeDescription(legendContext!!.nodes.nodeB.point.getAltitude()))
        }
    }
}
