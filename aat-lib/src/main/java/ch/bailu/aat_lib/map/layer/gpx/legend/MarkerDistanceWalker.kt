package ch.bailu.aat_lib.map.layer.gpx.legend

import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.preferences.StorageInterface

class MarkerDistanceWalker(storage: StorageInterface?, private val resetAfterDraw: Boolean) :
    LegendWalker() {
    private var distance = 0f
    private val description: DistanceDescription = DistanceDescription(storage!!)

    override fun doList(track: GpxList): Boolean {
        val legend = legendContext
        if (legend is LegendContext && super.doList(track)) {
            distance = 0f
            legend.setA(track.pointList.first as GpxPointNode?)
            if (legend.isAVisible) legend.drawNodeA()
            return true
        }
        return false
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        val isLast = marker.next == null
        val legend = legendContext
        if (legend is LegendContext && !isLast) {
            legend.setB(marker.firstNode as GpxPointNode)
            drawLegendFromB()
            if (!legend.arePointsTooClose()) {
                legend.switchNodes()
                if (resetAfterDraw) distance = 0f
            }
        }
        distance += marker.getDistance()
        return isLast
    }

    override fun doPoint(point: GpxPointNode) {
        if (point.next == null) {
            legendContext?.setB(point)
            drawLegendFromB()
        }
    }

    private fun drawLegendFromB() {
        val legend = legendContext
        if (legend is LegendContext && legend.isBVisible) {
            if (!legend.arePointsTooClose()) {
                legend.drawNodeB()
                legend.drawLabelB(description.getDistanceDescriptionN1(distance))
            }
        }
    }
}
