package ch.bailu.aat_lib.map.layer.gpx.legend

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.map.MapContext
import org.mapsforge.core.graphics.Paint

abstract class LegendWalker : GpxListWalker() {
    @JvmField
    var legendContext: LegendContext? = null

    fun init(mcontext: MapContext, backgroundPaint: Paint, framePaint: Paint) {
        legendContext = LegendContext(mcontext, backgroundPaint, framePaint)
    }

    override fun doList(track: GpxList): Boolean {
        return track.pointList.size() > 0 && legendContext?.isVisible(track.getDelta().getBoundingBox()) == true
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        return true
    }
}
