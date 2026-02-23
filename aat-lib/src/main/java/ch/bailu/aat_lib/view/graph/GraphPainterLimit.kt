package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.gpx.GpxPointNode

class GraphPainterLimit(p: GraphPlotter, private val segment: Segment, md: Int) :
    GraphPainter(p, md) {
    private var index = 0

    override fun doPoint(point: GpxPointNode) {
        if (segment.isInside(index)) {
            super.doPoint(point)
        }
        index++
    }
}
