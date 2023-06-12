package ch.bailu.aat.preferences.location

import android.content.Context
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.views.preferences.SolidTextInputDialog
import ch.bailu.aat_lib.coordinates.CH1903Coordinates
import ch.bailu.aat_lib.coordinates.Coordinates
import ch.bailu.aat_lib.coordinates.OlcCoordinates
import ch.bailu.aat_lib.coordinates.UTMCoordinates
import ch.bailu.aat_lib.coordinates.WGS84Coordinates
import ch.bailu.aat_lib.exception.ValidationException
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.preferences.SolidString
import ch.bailu.aat_lib.resources.Res
import org.mapsforge.core.model.LatLong

class SolidGoToLocation(val context: Context) : SolidString(
    Storage(context), KEY)
{

    companion object {
        private const val KEY = "GoToLocation"
    }

    private var reference: LatLong? = null

    override fun getLabel(): String {
        return Res.str().p_goto_location()
    }

    fun goToLocationFromUser(map: MapViewInterface) {
        reference = map.mapViewPosition.center
        SolidTextInputDialog(context, this, SolidTextInputDialog.TEXT) {
            goToLocation(
                map,
                valueAsString
            )
        }
    }

    fun goToLocation(map: MapViewInterface, s: String) {
        reference = map.mapViewPosition.center
        try {
            map.setCenter(latLongFromString(s))
        } catch (e: Exception) {
            AppLog.e(this, e)
        }
    }

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    private fun latLongFromString(code: String): LatLong {
        return try {
            if (reference != null) OlcCoordinates(code, reference).toLatLong() else OlcCoordinates(
                code
            ).toLatLong()
        } catch (eOLC: Exception) {
            try {
                CH1903Coordinates(code).toLatLong()
            } catch (eCH1903: Exception) {
                try {
                    WGS84Coordinates(code).toLatLong()
                } catch (eWGS: Exception) {
                    try {
                        UTMCoordinates(code).toLatLong()
                    } catch (eUTM: Exception) {
                        throw Coordinates.getCodeNotValidException(code)
                    }
                }
            }
        }
    }

    @Throws(ValidationException::class)
    override fun setValueFromString(string: String) {
        val stringTrimmed = string.trim { it <= ' ' }

        if (!validate(stringTrimmed)) {
            throw ValidationException(Res.str().p_goto_location_hint())
        } else {
            setValue(stringTrimmed)
        }
    }

    override fun validate(s: String): Boolean {
        return try {
            latLongFromString(s)
            true
        } catch (e: Exception) {
            false
        }
    }
}
