package ch.bailu.aat.map.mapsforge

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
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.map.layer.Layer

/**
 * MapContext for Android
 * For Background drawing: To draw inside map layers
 */
class MapsForgeContext(
    appContext: AppContext,
    private val map: MapsForgeViewBase,
    private val key: String,
    density: AppDensity
) : Layer(), MapContext, MapLayerInterface {

    private val metrics = MapsForgeMetrics(map, density)
    private val draw = AndroidDraw(metrics.getDensity(), appContext)
    private val nodes = TwoNodes(metrics)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun drawInside(mcontext: MapContext) {}
    override fun drawForeground(mcontext: MapContext) {}
    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun draw(
        boundingBox: BoundingBox,
        zoomLevel: Byte,
        canvas: Canvas,
        topLeftPoint: org.mapsforge.core.model.Point
    ) {
        metrics.init(boundingBox, zoomLevel, canvas.dimension, topLeftPoint)
        draw.init(canvas, metrics)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}

    override fun getMetrics(): MapMetrics {
        return metrics
    }

    override fun draw(): MapDraw {
        return draw
    }

    override fun getSolidKey(): String {
        return key
    }

    override fun getTwoNodes(): TwoNodes {
        return nodes
    }

    override fun getMapView(): MapViewInterface {
        return map
    }

    override fun onAttached() {}
    override fun onDetached() {}
}
