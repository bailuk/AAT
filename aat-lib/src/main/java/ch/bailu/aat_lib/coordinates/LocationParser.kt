package ch.bailu.aat_lib.coordinates

import ch.bailu.aat_lib.exception.IllegalCodeException
import org.mapsforge.core.model.LatLong

object LocationParser {

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun latLongFromString(code: String, reference: LatLong? = null): LatLong {
        val trimmedCode = code.trim()
        return try {
            if (reference != null) {
                OlcCoordinates(trimmedCode, reference).toLatLong()
            } else {
                OlcCoordinates(trimmedCode).toLatLong()
            }
        } catch (eOLC: Exception) {
            try {
                CH1903Coordinates(trimmedCode).toLatLong()
            } catch (eCH1903: Exception) {
                try {
                    WGS84Coordinates(trimmedCode).toLatLong()
                } catch (eWGS: Exception) {
                    try {
                        UTMCoordinates(trimmedCode).toLatLong()
                    } catch (eUTM: Exception) {
                        throw IllegalCodeException(trimmedCode)
                    }
                }
            }
        }
    }

    fun validate(s: String): Boolean {
        return try {
            latLongFromString(s)
            true
        } catch (e: Exception) {
            false
        }
    }
}
