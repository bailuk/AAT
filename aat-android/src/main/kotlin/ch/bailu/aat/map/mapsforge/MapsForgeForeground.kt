package ch.bailu.aat.map.mapsforge

import android.graphics.Canvas
import ch.bailu.aat.map.AndroidDraw
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.AppDensity
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.MapDraw
import ch.bailu.aat_lib.map.MapMetrics
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.map.MapsForgeMetrics
import ch.bailu.aat_lib.map.TwoNodes
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.service.ServicesInterface
import org.mapsforge.core.model.Dimension
import org.mapsforge.map.android.view.MapView

/**
 * MapContext for Android
 * For Foreground drawing: To draw over map layers
 */
class MapsForgeForeground(
    appContext: AppContext,
    mapView: MapView,
    private val mcontext: MapContext,
    d: AppDensity,
    private val layers: ArrayList<MapLayerInterface>
) : MapContext {

    private val draw: AndroidDraw
    private val metrics: MapsForgeMetrics

    init {
        metrics = MapsForgeMetrics(mapView, d)
        draw = AndroidDraw(mcontext.getMetrics().density, appContext)
    }

    fun dispatchDraw(services: ServicesInterface, canvas: Canvas) {
        services.insideContext {
            metrics.init(Dimension(canvas.width, canvas.height))
            draw.init(canvas, metrics)
            for (l in layers) {
                l.drawForeground(this@MapsForgeForeground)
            }
        }
    }

    override fun getMetrics(): MapMetrics {
        return metrics
    }

    override fun draw(): MapDraw {
        return draw
    }

    override fun getSolidKey(): String {
        return mcontext.getSolidKey()
    }

    override fun getTwoNodes(): TwoNodes {
        return mcontext.getTwoNodes()
    }

    override fun getMapView(): MapViewInterface {
        return mcontext.getMapView()
    }
}
