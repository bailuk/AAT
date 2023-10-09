package ch.bailu.aat_lib.map.layer.grid

import ch.bailu.aat_lib.coordinates.MeterCoordinates
import ch.bailu.aat_lib.coordinates.UTMCoordinates
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.model.LatLong

class UTMGridLayer(storage: StorageInterface) : MeterGridLayer(storage) {
    override fun getCoordinates(point: LatLong): MeterCoordinates {
        return UTMCoordinates(point)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun onAttached() {}
    override fun onDetached() {}
}
