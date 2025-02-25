package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitude
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitudeValue
import ch.bailu.aat_lib.preferences.location.SolidProvideAltitude

class AdjustGpsAltitude(next: LocationStackItem, storage: StorageInterface) :
    LocationStackChainedItem(next) {
    private val saltitude = SolidProvideAltitude(storage, SolidUnit.SI)

    private val senabled = SolidAdjustGpsAltitude(storage)
    private val sadjust = SolidAdjustGpsAltitudeValue(storage)

    private var adjust: Int
    private var enabled: Boolean

    private val altitude = AltitudeCache()

    init {
        adjust = sadjust.getValue()
        enabled = senabled.isEnabled
    }

    override fun passLocation(location: LocationInformation) {
        if (altitude.set(location) && enabled) {
            location.setAltitude(location.getAltitude() + adjust)
        }
        super.passLocation(location)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String, presetIndex: Int) {
        if (senabled.hasKey(key)) {
            enabled = senabled.value
        } else if (sadjust.hasKey(key)) {
            adjust = sadjust.getValue()
        } else if (saltitude.hasKey(key)) {
            altitude.setGPSAdjustValue(sadjust, saltitude.getValue())
        }
    }

    private class AltitudeCache {
        private var altitude = 0
        private var time: Long = 0

        fun set(l: LocationInformation): Boolean {
            if (l.hasAltitude()) {
                altitude = l.getAltitude().toInt()
                time = l.getTimeStamp()
                return true
            }
            return false
        }

        fun setGPSAdjustValue(sadjust: SolidAdjustGpsAltitudeValue, currentAltitude: Int) {
            val age = System.currentTimeMillis() - time

            if (age < MAX_AGE) {
                sadjust.setValue(currentAltitude - altitude)
            }
        }

        companion object {
            private const val MAX_AGE = (10 * 1000).toLong()
        }
    }
}
