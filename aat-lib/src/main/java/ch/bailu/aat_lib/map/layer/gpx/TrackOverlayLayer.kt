package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.TwoNodes
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.graphics.Cap
import org.mapsforge.core.graphics.Join
import org.mapsforge.core.graphics.Paint

class TrackOverlayLayer(private val mcontext: MapContext) : GpxLayer() {
    private val paint: Paint = AppGraphicFactory.instance().createPaint()

    init {
        paint.strokeWidth =
            mcontext.getMetrics().getDensity().toPixelFloat(STROKE_WIDTH.toFloat())
        paint.setStrokeCap(Cap.ROUND)
        paint.setStrokeJoin(Join.ROUND)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun drawInside(mcontext: MapContext) {
        TrackPainter().walkTrack(gpxList)
    }

    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun onAttached() {}
    override fun onDetached() {}
    private inner class TrackPainter : GpxListPainter(mcontext) {
        override fun drawEdge(nodes: TwoNodes) {
            mcontext.draw().edge(nodes, paint)
        }

        override fun drawNode(node: TwoNodes.PixelNode) {
            paint.color = getColor()
        }
    }

    companion object {
        private const val STROKE_WIDTH = 3
    }
}
