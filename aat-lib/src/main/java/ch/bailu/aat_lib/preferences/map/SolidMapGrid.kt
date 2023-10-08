package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.coordinates.CH1903Coordinates
import ch.bailu.aat_lib.coordinates.OlcCoordinates
import ch.bailu.aat_lib.coordinates.WGS84Coordinates.Companion.getGeoUri
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.map.layer.NullLayer
import ch.bailu.aat_lib.map.layer.grid.CH1903CenterCoordinatesLayer
import ch.bailu.aat_lib.map.layer.grid.CH1903GridLayer
import ch.bailu.aat_lib.map.layer.grid.PlusCodesCenterCoordinatesLayer
import ch.bailu.aat_lib.map.layer.grid.UTMCenterCoordinatesLayer
import ch.bailu.aat_lib.map.layer.grid.UTMGridLayer
import ch.bailu.aat_lib.map.layer.grid.WGS84Layer
import ch.bailu.aat_lib.preferences.SolidStaticIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import org.mapsforge.core.model.LatLong

class SolidMapGrid(storage: StorageInterface, key: String) : SolidStaticIndexList(
    storage, key + POSTFIX, LABEL
) {
    override fun getIconResource(): String {
        return "view_grid"
    }

    fun createGridLayer(services: ServicesInterface): MapLayerInterface {
        if (index == 0) {
            return WGS84Layer(services, getStorage())
        }
        if (index == 1) {
            return CH1903GridLayer(getStorage())
        }
        if (index == 2) {
            return UTMGridLayer(getStorage())
        }
        return if (index == 3) {
            PlusCodesCenterCoordinatesLayer(services, getStorage())
        } else NullLayer()
    }

    fun createCenterCoordinatesLayer(services: ServicesInterface): MapLayerInterface {
        if (index == 1) {
            return CH1903CenterCoordinatesLayer(services, getStorage())
        }
        return if (index == 2) {
            UTMCenterCoordinatesLayer(services, getStorage())
        } else NullLayer()
    }

    val clipboardLabel: CharSequence
        get() = if (index == 3 || index == 1) {
            LABEL[index]
        } else LABEL[0]

    fun getCode(pos: LatLong): CharSequence {
        if (index == 3) {
            return OlcCoordinates(pos).toString()
        } else if (index == 1) {
            return CH1903Coordinates(pos).toString()
        }
        return getGeoUri(pos)
    }

    companion object {
        private const val POSTFIX = "_GRID"
        private val LABEL =
            arrayOf("WGS84", "CH1903", "UTM", "Open Location Code (plus codes)", "None")
    }
}
