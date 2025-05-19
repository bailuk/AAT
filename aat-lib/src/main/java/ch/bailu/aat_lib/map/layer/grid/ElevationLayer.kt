package ch.bailu.aat_lib.map.layer.grid

import ch.bailu.aat_lib.description.AltitudeDescription
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.model.LatLong

class ElevationLayer(private val services: ServicesInterface, storage: StorageInterface?) : MapLayerInterface {
    private val altitudeDescription: AltitudeDescription = AltitudeDescription(storage!!)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun drawInside(mcontext: MapContext) {}
    override fun drawForeground(mcontext: MapContext) {
        val zoomLevel = mcontext.getMetrics().getZoomLevel().toByte()
        val point = mcontext.getMapView().getMapViewPosition().center
        drawElevation(services, mcontext, zoomLevel.toInt(), point)
    }

    private fun drawElevation(sc: ServicesInterface, mc: MapContext, zoom: Int, point: LatLong) {
        if (zoom > MIN_ZOOM_LEVEL) {
            sc.insideContext {
                val ele = sc.getElevationService().getElevation(point.latitudeE6, point.longitudeE6)
                mc.draw().textBottom(altitudeDescription.getValueUnit(ele.toFloat()), 2)
            }
        }
    }

    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun onAttached() {}
    override fun onDetached() {}

    companion object {
        private const val MIN_ZOOM_LEVEL = 7
    }
}
