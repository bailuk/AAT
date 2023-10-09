package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.map.MapContext

class GpxVisibleLimit(private val mcontext: MapContext) : GpxListWalker() {
    private var index = 0
    var firstPoint = -1
        private set
    var lastPoint = -1
        private set

    override fun doList(track: GpxList): Boolean {
        return mcontext.getMetrics().isVisible(track.getDelta().getBoundingBox())
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        val visible = mcontext.getMetrics().isVisible(segment.getBoundingBox())
        if (!visible) {
            index += segment.segmentSize
        }
        return visible
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        val visible = mcontext.getMetrics().isVisible(marker.getBoundingBox())
        if (!visible) {
            index += marker.segmentSize
        }
        return visible
    }

    override fun doPoint(point: GpxPointNode) {
        val visible = mcontext.getMetrics().isVisible(point)
        if (visible) {
            if (firstPoint < 0) {
                firstPoint = index
            }
            lastPoint = index
        }
        index++
    }
}
