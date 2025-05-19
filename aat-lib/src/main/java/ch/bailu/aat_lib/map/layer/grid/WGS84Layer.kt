package ch.bailu.aat_lib.map.layer.grid

import ch.bailu.aat_lib.coordinates.WGS84Coordinates
import ch.bailu.aat_lib.description.FF.Companion.f
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.model.LatLong

class WGS84Layer(services: ServicesInterface, storage: StorageInterface) : MapLayerInterface {
    private val elevation: ElevationLayer = ElevationLayer(services, storage)
    private val crosshair: Crosshair = Crosshair()

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

    private fun drawCoordinates(mapContext: MapContext, point: LatLong) {
        val f = f()
        mapContext.draw().textBottom(WGS84Coordinates(point).toString(), 1)
        mapContext.draw()
            .textBottom(f.N6.format(point.latitude) + "/" + f.N6.format(point.getLongitude()), 0)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun onAttached() {}
    override fun onDetached() {}
}
