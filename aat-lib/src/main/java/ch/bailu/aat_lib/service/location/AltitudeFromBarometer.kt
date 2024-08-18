package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface
import javax.annotation.Nonnull

class AltitudeFromBarometer(next: LocationStackItem,
                            private val sensorService: SensorServiceInterface
) : LocationStackChainedItem(next) {
    override fun passLocation(location: LocationInformation) {
        val info = sensorService.getInformationOrNull(InfoID.BAROMETER_SENSOR)

        if (info != null) {
            val altitude = info.getAltitude()
            location.setAltitude(altitude)
        }

        super.passLocation(location)
    }
}
