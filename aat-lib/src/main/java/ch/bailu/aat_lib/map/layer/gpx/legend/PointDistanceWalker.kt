package ch.bailu.aat_lib.map.layer.gpx.legend

import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.preferences.StorageInterface

class PointDistanceWalker(storage: StorageInterface?, private val resetAfterDraw: Boolean) :
    LegendWalker() {
    private val description: DistanceDescription
    private var distance = 0f

    init {
        description = DistanceDescription(storage!!)
    }

    override fun doList(track: GpxList): Boolean {
        distance = 0f
        return super.doList(track)
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return if (legendContext!!.isVisible(marker.getBoundingBox())) {
            distance -= (marker.firstNode as GpxPointNode).getDistance()
            true
        } else {
            distance += marker.getDistance()
            false
        }
    }

    override fun doPoint(point: GpxPointNode) {
        legendContext!!.setB(point)
        distance += point.getDistance()
        if (!legendContext!!.arePointsTooClose()) {
            drawLegendFromB()
            legendContext!!.switchNodes()
            if (resetAfterDraw) distance = 0f
        }
    }

    private fun drawLegendFromB() {
        if (legendContext!!.isBVisible) {
            legendContext!!.drawLabelB(description.getDistanceDescriptionN1(distance))
        }
    }
}
