package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode

class GraphPainterMarkerMode(p: GraphPlotter, md: Int) : GraphPainter(p, md) {
    override fun doMarker(marker: GpxSegmentNode): Boolean {
        if (marker.firstNode is GpxPointNode) {
            plotIfDistance(marker.firstNode)
            incrementSummaryDistance(marker.getDistance())
        }
        return false
    }
}
