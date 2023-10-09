package ch.bailu.aat_lib.map.layer.grid

import ch.bailu.aat_lib.coordinates.MeterCoordinates
import ch.bailu.aat_lib.coordinates.UTMCoordinates
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import org.mapsforge.core.model.LatLong

class UTMCenterCoordinatesLayer(services: ServicesInterface, storage: StorageInterface) :
    CenterCoordinatesLayer(services, storage) {
    override fun getCoordinates(p: LatLong): MeterCoordinates {
        return UTMCoordinates(p)
    }
}
