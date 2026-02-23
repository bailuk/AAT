package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode

open class ScaleGenerator(private val plotter: GraphPlotter) : GpxListWalker() {
    override fun doPoint(point: GpxPointNode) {
        plotter.includeInYScale(point.getAltitude())
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return true
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        return true
    }

    override fun doList(track: GpxList): Boolean {
        return true
    }
}
