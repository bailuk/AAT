package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.resources.Res
import java.text.DecimalFormat

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


    fun getValue(v: Float): String {
        val f = unit.altitudeFactor
        return f0.format((v * f).toDouble())
    }

    fun getValueUnit(v: Float): String {
        return getValue(v) + " " + getUnit()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(info.altitude.toFloat())
    }

    companion object {
        private val f0 = DecimalFormat("0")
    }
}
