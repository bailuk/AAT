package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitude
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.sensor.SensorState

class AltitudeConfigurationDescription(storage: StorageInterface) : AltitudeDescription(
    storage
) {
    private var configuration: String = ""
    private var haveSensor = SensorState.isConnected(InfoID.BAROMETER_SENSOR)
    private val sadjustAltitude = SolidAdjustGpsAltitude(storage)

    init {
        setLabel()
    }

    private fun setLabel() {
        configuration = if (haveSensor) {
            " " + Res.str().sensor_barometer()
        } else if (sadjustAltitude.isEnabled) {
            " GPS+-"
        } else {
            " GPS"
        }
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val state = SensorState.isConnected(InfoID.BAROMETER_SENSOR)
        if (haveSensor != state) {
            haveSensor = state
            setLabel()
        }
        super.onContentUpdated(iid, info)
    }

    override fun getLabel(): String {
        return super.getLabel() + configuration
    }
}
