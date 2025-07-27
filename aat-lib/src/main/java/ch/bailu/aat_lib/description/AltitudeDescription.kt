package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.resources.Res

open class AltitudeDescription(storageInterface: StorageInterface) : FloatDescription() {
    private val unit = SolidUnit(storageInterface)
    override fun getLabel(): String {
        return Res.str().altitude()
    }

    override fun getUnit(): String {
        return unit.altitudeUnit
    }

    override fun getValue(): String {
        return getValue(cache)
    }

    fun getValue(value: Float): String {
        return FormatDisplay.f().decimal1.format(value * unit.altitudeFactor)
    }

    fun getValueUnit(v: Float): String {
        return getValue(v) + " " + getUnit()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(info.getAltitude())
    }

    fun getAltitudeDescription(value: Float): String {
        return getValue(value) + " " + unit.altitudeUnit
    }
}
