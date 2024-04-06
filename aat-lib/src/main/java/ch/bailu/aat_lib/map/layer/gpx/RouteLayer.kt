package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.lib.color.ColorInterface
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.MapPaint.createEdgePaintBlur
import ch.bailu.aat_lib.map.MapPaint.createEdgePaintLine
import ch.bailu.aat_lib.map.layer.gpx.painter.EdgePainter
import ch.bailu.aat_lib.map.layer.gpx.painter.RoutePainterNode

class RouteLayer(mcontext: MapContext) : GpxLayer() {
    companion object {
        private const val MIN_PIXEL_SPACE = 30
    }

    private var paint = createEdgePaintLine(mcontext.getMetrics().getDensity())
    private var color = colorFromIID
    private var zoom = 0
    private var shadow = createEdgePaintBlur(mcontext.draw(), color, zoom)

    override fun drawInside(mcontext: MapContext) {
        if (zoom != mcontext.getMetrics().getZoomLevel() || color != colorFromIID) {
            zoom = mcontext.getMetrics().getZoomLevel()
            color = colorFromIID
            paint = createEdgePaintLine(mcontext.getMetrics().getDensity())
            shadow = createEdgePaintBlur(mcontext.draw(), ColorInterface.BLACK, zoom)
            paint.color = color
        }
        EdgePainter(mcontext, shadow, MIN_PIXEL_SPACE).walkTrack(gpxList)
        EdgePainter(mcontext, paint, MIN_PIXEL_SPACE).walkTrack(gpxList)
        RoutePainterNode(mcontext, color, MIN_PIXEL_SPACE).walkTrack(gpxList)
    }
}

