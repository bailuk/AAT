package ch.bailu.aat_lib.map.layer.gpx.legend

import ch.bailu.aat_lib.lib.color.ARGB
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.MapMetrics
import ch.bailu.aat_lib.map.MapPaint.createBackgroundPaint
import ch.bailu.aat_lib.map.MapPaint.createEdgePaintLine
import ch.bailu.aat_lib.map.layer.gpx.GpxLayer
import org.mapsforge.core.graphics.Paint

class GpxLegendLayer(private val walker: LegendWalker) : GpxLayer() {
    private var backgroundPaint = createBackgroundPaint()
    private var framePaint: Paint = createBackgroundPaint()
    private var color = ARGB.WHITE

    init {
        initPaint()
    }

    override fun drawInside(mcontext: MapContext) {
        if (color != colorFromIID) {
            color = colorFromIID
            initPaint(mcontext.getMetrics())
        }
        walker.init(mcontext, backgroundPaint, framePaint)
        walker.walkTrack(gpxList)
    }

    private fun initPaint(metrics: MapMetrics) {
        backgroundPaint = createBackgroundPaint(color)
        framePaint = createEdgePaintLine(metrics.getDensity())
        framePaint.color = color
    }

    private fun initPaint() {
        backgroundPaint = createBackgroundPaint(color)
        framePaint = createBackgroundPaint(color)
    }
}
