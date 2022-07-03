package ch.bailu.aat_gtk.view.map

import ch.bailu.aat_gtk.app.GtkAppDensity
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.*
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import org.mapsforge.core.graphics.Canvas
import java.util.*

class GtkMapContextForeground(
    appContext: AppContext,
    private val metrics: MapsForgeMetrics,
    private val mcontext: MapContext,
    private val layers: ArrayList<MapLayerInterface>) : MapContext {

    private val draw = GtkMapDraw(NodeBitmap.get(GtkAppDensity, appContext))

    fun dispatchDraw(canvas: Canvas) {
        metrics.init(canvas.dimension)
        draw.init(canvas, metrics)
        for (l in layers) {
            l.drawForeground(this)
        }
    }

    override fun getMetrics(): MapMetrics {
        return metrics
    }

    override fun draw(): MapDraw {
        return draw
    }


    override fun getSolidKey(): String {
        return mcontext.solidKey
    }

    override fun getTwoNodes(): TwoNodes {
        return mcontext.twoNodes
    }

    override fun getMapView(): MapViewInterface {
        return mcontext.mapView
    }


}