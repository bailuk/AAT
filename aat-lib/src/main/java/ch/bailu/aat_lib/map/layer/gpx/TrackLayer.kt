package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.app.AppGraphicFactory.instance
import ch.bailu.aat_lib.lib.color.AltitudeColorTable
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.TwoNodes
import ch.bailu.aat_lib.map.TwoNodes.PixelNode
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.graphics.Cap
import org.mapsforge.core.graphics.Join
import org.mapsforge.core.graphics.Paint

class TrackLayer(private val mcontext: MapContext) : GpxLayer() {
    private val paint: Paint = instance().createPaint()

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

        override fun drawNode(node: PixelNode) {
            val altitude = node.point.getAltitude().toInt()
            val color = AltitudeColorTable.instance().getColor(altitude)
            paint.color = color
        }
    }

    companion object {
        private const val STROKE_WIDTH = 3
    }
}
