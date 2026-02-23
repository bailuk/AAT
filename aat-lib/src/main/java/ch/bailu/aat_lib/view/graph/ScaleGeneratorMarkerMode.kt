package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode

class ScaleGeneratorMarkerMode(p: GraphPlotter) : ScaleGenerator(p) {
    override fun doMarker(marker: GpxSegmentNode): Boolean {
        val point = marker.firstNode
        if (point is GpxPointNode) {
            doPoint(point)
        }
        return false
    }
}
