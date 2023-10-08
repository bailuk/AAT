package ch.bailu.aat_lib.map.layer.grid

import ch.bailu.aat_lib.coordinates.Coordinates
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.model.LatLong

abstract class CenterCoordinatesLayer(services: ServicesInterface?, storage: StorageInterface?) :
    MapLayerInterface {
    private val elevation: ElevationLayer

    init {
        elevation = ElevationLayer(services, storage)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun drawForeground(mcontext: MapContext) {
        val point = mcontext.getMapView().getMapViewPosition().center
        mcontext.draw().textBottom(getCoordinates(point).toString(), 1)
        elevation.drawForeground(mcontext)
    }

    override fun drawInside(mc: MapContext) {}
    override fun onTap(tapXY: Point): Boolean {
        return false
    }

    abstract fun getCoordinates(p: LatLong): Coordinates
    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun onAttached() {}
    override fun onDetached() {}
}
