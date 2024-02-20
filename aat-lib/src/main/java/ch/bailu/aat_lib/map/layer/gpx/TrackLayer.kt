package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.app.AppGraphicFactory.instance
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.gpx.painter.TrackPainter
import ch.bailu.aat_lib.preferences.StorageInterface
import org.mapsforge.core.graphics.Cap
import org.mapsforge.core.graphics.Join
import org.mapsforge.core.graphics.Paint

class TrackLayer(mcontext: MapContext) : GpxLayer() {
    companion object {
        private const val STROKE_WIDTH = 3
    }

    private val paint: Paint = instance().createPaint()

    init {
        paint.strokeWidth =
            mcontext.getMetrics().getDensity().toPixelFloat(STROKE_WIDTH.toFloat())
        paint.setStrokeCap(Cap.ROUND)
        paint.setStrokeJoin(Join.ROUND)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun drawInside(mcontext: MapContext) {
        TrackPainter(mcontext, paint).walkTrack(gpxList)
    }
}
