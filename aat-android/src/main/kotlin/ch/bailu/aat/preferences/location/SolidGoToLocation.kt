package ch.bailu.aat.preferences.location

import android.content.Context
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.views.preferences.dialog.SolidTextInputDialog
import ch.bailu.aat_lib.coordinates.LocationParser
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
        reference = map.getMapViewPosition().center
        SolidTextInputDialog(context,this, SolidTextInputDialog.TEXT) {
            goToLocation(map, getValueAsString())
        }
    }

    fun goToLocation(map: MapViewInterface, s: String) {
        reference = map.getMapViewPosition().center
        try {
            map.setCenter(LocationParser.latLongFromString(s))
        } catch (e: Exception) {
            AppLog.e(this, e)
        }
    }

    @Throws(ValidationException::class)
    override fun setValueFromString(string: String) {
        if (!validate(string)) {
            throw ValidationException(Res.str().p_goto_location_hint())
        } else {
            setValue(string)
        }
    }
}
