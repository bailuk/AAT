package ch.bailu.aat_lib.preferences.location

import ch.bailu.aat_lib.coordinates.LatLongE6
import ch.bailu.aat_lib.coordinates.LatLongInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import org.mapsforge.core.model.LatLong

object SolidMapPosition {
    private const val LONGITUDE_SUFFIX = "longitude"
    private const val LATITUDE_SUFFIX = "latitude"
    private const val ZOOM_SUFFIX = "zoom"

    @JvmStatic
    fun readPosition(storage: StorageInterface, key: String): LatLongE6 {
        return LatLongE6(
            storage.readInteger(key + LATITUDE_SUFFIX),
            storage.readInteger(key + LONGITUDE_SUFFIX)
        )
    }

    fun readZoomLevel(storage: StorageInterface, key: String): Int {
        return storage.readInteger(key + ZOOM_SUFFIX)
    }

    fun writeZoomLevel(storage: StorageInterface, key: String, zoom: Int) {
        storage.writeInteger(key + ZOOM_SUFFIX, zoom)
    }

    fun writePosition(storage: StorageInterface, key: String, latLong: LatLong) {
        writePosition(storage, key, LatLongE6(latLong))
    }

    @JvmStatic
    fun writePosition(storage: StorageInterface, key: String, latLong: LatLongInterface) {
        storage.writeInteger(key + LATITUDE_SUFFIX, latLong.getLatitudeE6())
        storage.writeInteger(key + LONGITUDE_SUFFIX, latLong.getLongitudeE6())
    }
}
