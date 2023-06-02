package ch.bailu.aat_gtk.view.map

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.AppDensity
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.MapDraw
import ch.bailu.aat_lib.map.MapMetrics
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.map.MapsForgeMetrics
import ch.bailu.aat_lib.map.NodeBitmap
import ch.bailu.aat_lib.map.TwoNodes
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import org.mapsforge.core.graphics.Canvas

class GtkMapContextForeground(
    appContext: AppContext,
    appDensity: AppDensity,
    private val metrics: MapsForgeMetrics,
    private val mcontext: MapContext,
    private val layers: ArrayList<MapLayerInterface>) : MapContext {

    private val draw = GtkMapDraw(appDensity, NodeBitmap.get(appDensity, appContext))

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
