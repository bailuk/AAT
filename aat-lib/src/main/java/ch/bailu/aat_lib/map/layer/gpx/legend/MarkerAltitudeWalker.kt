package ch.bailu.aat_lib.map.layer.gpx.legend

import ch.bailu.aat_lib.description.AltitudeDescription
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.preferences.StorageInterface

class MarkerAltitudeWalker(storage: StorageInterface) : LegendWalker() {
    private val description = AltitudeDescription(storage)

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        val isLast = marker.next == null
        val legend = legendContext
        if (legend is LegendContext && !isLast) {
            legend.nodes.nodeB.set((marker.firstNode as GpxPointNode))
            drawLegendFromB()
            if (!legend.arePointsTooClose()) {
                legend.nodes.switchNodes()
            }
        }
        return isLast
    }

    override fun doPoint(point: GpxPointNode) {
        if (point.next == null) {
            legendContext?.nodes?.nodeB?.set(point)
            drawLegendFromB()
        }
    }

    private fun drawLegendFromB() {
        val legend = legendContext
        if (legend is LegendContext && legend.isBVisible) {
            if (!legend.arePointsTooClose()) {
                legend.drawNodeB()
                legend.drawLabelB(description.getAltitudeDescription(legend.nodes.nodeB.point.getAltitude()))
            }
        }
    }
}
