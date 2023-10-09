package ch.bailu.aat_lib.map.layer.gpx

import ch.bailu.aat_lib.lib.color.AltitudeColorTable
import ch.bailu.aat_lib.lib.color.ColorInterface
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.MapPaint.createEdgePaintBlur
import ch.bailu.aat_lib.map.MapPaint.createEdgePaintLine
import ch.bailu.aat_lib.map.TwoNodes
import ch.bailu.aat_lib.map.TwoNodes.PixelNode
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.elevation.ElevationProvider
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.graphics.Paint

class RouteLayer(private val mcontext: MapContext) : GpxLayer() {
    private var paint: Paint
    private var shadow: Paint
    private var color: Int
    private var zoom = 0

    init {
        color = getColor()
        paint = createEdgePaintLine(mcontext.getMetrics().getDensity())
        shadow = createEdgePaintBlur(mcontext.draw(), color, zoom)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun drawInside(mcontext: MapContext) {
        if (zoom != mcontext.getMetrics().getZoomLevel() || color != getColor()) {
            zoom = mcontext.getMetrics().getZoomLevel()
            color = getColor()
            paint = createEdgePaintLine(mcontext.getMetrics().getDensity())
            shadow = createEdgePaintBlur(mcontext.draw(), ColorInterface.BLACK, zoom)
            paint.color = color
        }
        RouteShadowPainter().walkTrack(gpxList)
        RoutePainterEdge().walkTrack(gpxList)
        RoutePainterNode().walkTrack(gpxList)
    }

    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun onAttached() {}
    override fun onDetached() {}
    private inner class RouteShadowPainter : GpxListPainter(mcontext, MIN_PIXEL_SPACE) {
        override fun drawEdge(nodes: TwoNodes) {
            mcontext.draw().edge(nodes, shadow)
        }

        override fun drawNode(node: PixelNode) {}
    }

    private inner class RoutePainterNode : GpxListPainter(mcontext, MIN_PIXEL_SPACE) {
        override fun drawEdge(nodes: TwoNodes) {}
        override fun drawNode(node: PixelNode) {
            val c: Int
            val altitude = node.point.getAltitude().toInt()
            c =
                if (altitude == ElevationProvider.NULL_ALTITUDE) getColor() else AltitudeColorTable.instance()
                    .getColor(altitude)
            mcontext.draw().bitmap(mcontext.draw().getNodeBitmap(), node.pixel, c)
        }
    }

    private inner class RoutePainterEdge : GpxListPainter(mcontext, MIN_PIXEL_SPACE) {
        override fun drawEdge(nodes: TwoNodes) {
            mcontext.draw().edge(nodes, paint)
        }

        override fun drawNode(node: PixelNode) {}
    }

    companion object {
        private const val MIN_PIXEL_SPACE = 30
    }
}
