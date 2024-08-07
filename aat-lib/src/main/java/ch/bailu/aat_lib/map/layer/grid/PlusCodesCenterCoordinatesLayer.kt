package ch.bailu.aat_lib.map.layer.grid

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Point
import com.google.openlocationcode.OpenLocationCode
import org.mapsforge.core.model.LatLong

class PlusCodesCenterCoordinatesLayer(
    services: ServicesInterface,
    storage: StorageInterface
) : MapLayerInterface {
    private val elevation: ElevationLayer
    private val crosshair: Crosshair

    init {
        elevation = ElevationLayer(services, storage)
        crosshair = Crosshair()
    }

    override fun drawForeground(mcontext: MapContext) {
        val point = mcontext.getMapView().getMapViewPosition().center
        crosshair.drawForeground(mcontext)
        drawCoordinates(mcontext, point)
        elevation.drawForeground(mcontext)
    }

    override fun drawInside(mcontext: MapContext) {}
    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    private fun drawCoordinates(clayer: MapContext, point: LatLong) {
        val center = OpenLocationCode(point.latitude, point.longitude)
        val code = center.code
        clayer.draw().textBottom(code, 1)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun onAttached() {}
    override fun onDetached() {}
}
