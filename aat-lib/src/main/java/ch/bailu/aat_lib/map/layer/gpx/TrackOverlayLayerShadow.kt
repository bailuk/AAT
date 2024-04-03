package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.lib.color.ColorInterface
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.MapPaint
import ch.bailu.aat_lib.map.layer.gpx.painter.EdgePainter

class TrackOverlayLayerShadow(mcontext: MapContext) : GpxLayer() {

    companion object {
        private const val MIN_PIXEL_SPACE = 30
    }

    private var paint = MapPaint.createEdgePaintLine(mcontext.getMetrics().getDensity())
    private var color = colorFromIID
    private var zoom = 0
    private var shadow = MapPaint.createEdgePaintBlur(mcontext.draw(), color, zoom)

    override fun drawInside(mcontext: MapContext) {
        if (zoom != mcontext.getMetrics().getZoomLevel() || color != colorFromIID) {
            zoom = mcontext.getMetrics().getZoomLevel()
            color = colorFromIID
            paint = MapPaint.createEdgePaintLine(mcontext.getMetrics().getDensity())
            shadow = MapPaint.createEdgePaintBlur(mcontext.draw(), ColorInterface.BLACK, zoom)
            paint.color = color
        }
        EdgePainter(mcontext, shadow, MIN_PIXEL_SPACE).walkTrack(gpxList)
        EdgePainter(mcontext, paint, MIN_PIXEL_SPACE).walkTrack(gpxList)
    }}
