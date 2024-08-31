package ch.bailu.aat.preferences.location

import android.content.Context
import android.view.View
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.views.preferences.dialog.SolidTextInputDialog
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitude
import ch.bailu.aat_lib.preferences.location.SolidProvideAltitude
import ch.bailu.aat_lib.service.sensor.SensorState

object AndroidSolidAltitudeUtil {
    @JvmStatic
    fun requestOnClick(v: View): View {
        v.setOnClickListener { v1: View -> requestValueFromUserIfEnabled(v1.context) }
        return v
    }

    private fun requestValueFromUserIfEnabled(context: Context) {
        if (SensorState.isConnected(InfoID.BAROMETER_SENSOR) || SolidAdjustGpsAltitude(Storage(context)).isEnabled) {
            requestValueFromUser(context)
        }
    }

    private fun requestValueFromUser(context: Context) {
        SolidTextInputDialog(
            context,
            SolidProvideAltitude(
                Storage(context),
                SolidUnit(Storage(context)).index
            ),
            SolidTextInputDialog.INTEGER_SIGNED
        )
    }
}
