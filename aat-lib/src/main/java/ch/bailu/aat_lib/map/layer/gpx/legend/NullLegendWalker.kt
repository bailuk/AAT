package ch.bailu.aat_lib.map.layer.gpx.legend

import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode

class NullLegendWalker : LegendWalker() {
    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return false
    }

    override fun doPoint(point: GpxPointNode) {}
}
