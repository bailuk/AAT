package ch.bailu.aat_lib.map.layer.grid

import ch.bailu.aat_lib.coordinates.CH1903Coordinates
import ch.bailu.aat_lib.coordinates.CH1903Coordinates.Companion.inSwitzerland
import ch.bailu.aat_lib.coordinates.MeterCoordinates
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import org.mapsforge.core.model.LatLong

class CH1903CenterCoordinatesLayer(services: ServicesInterface, storage: StorageInterface) :
    CenterCoordinatesLayer(services, storage) {
    override fun getCoordinates(p: LatLong): MeterCoordinates {
        return CH1903Coordinates(p)
    }

    override fun drawForeground(mcontext: MapContext) {
        if (inSwitzerland(mcontext.getMetrics().getBoundingBox().centerPoint)) super.drawForeground(
            mcontext
        )
    }
}
