package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.MapPaint
import ch.bailu.aat_lib.map.layer.gpx.painter.EdgePainter

class TrackOverlayLayer(mcontext: MapContext) : GpxLayer() {

    companion object {
        private const val MIN_PIXEL_SPACE = 30
    }

    private var paint = MapPaint.createEdgePaintLine(mcontext.getMetrics().getDensity())
    private var color = colorFromIID

    override fun drawInside(mcontext: MapContext) {
        if (color != colorFromIID) {
            color = colorFromIID
            paint = MapPaint.createEdgePaintLine(mcontext.getMetrics().getDensity())
            paint.strokeWidth *= 3
            paint.color = color
        }
        EdgePainter(mcontext, paint, MIN_PIXEL_SPACE).walkTrack(gpxList)
    }
}
