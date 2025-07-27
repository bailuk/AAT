package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.description.FormatDisplay.Companion.f
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.resources.Res
import java.util.Objects

open class DistanceDescription(storage: StorageInterface) : FloatDescription() {
    private val format = arrayOf(f().decimal3, f().decimal2, f().decimal1, f().decimal0)
    private val unit: SolidUnit

    init {
        Objects.requireNonNull(storage)
        unit = SolidUnit(storage)
    }

    override fun getLabel(): String {
        return Res.str().distance()
    }

    override fun getUnit(): String {
        return unit.distanceUnit
    }

    override fun getValue(): String {
        val dist = unit.distanceFactor * cache
        var format = 0
        var x = 10
        while (dist >= x && format < this.format.size - 1) {
            format++
            x *= 10
        }
        return this.format[format].format(dist.toDouble())
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(info.getDistance())
    }

    fun getDistanceDescription(distance: Float): String {
        val nonSI = distance * unit.distanceFactor
        return if (nonSI < 1) getShortDistanceDescription(distance) else f().decimal0.format(nonSI) + " " + unit.distanceUnit
    }

    fun getDistanceDescriptionN1(distance: Float): String {
        val nonSI = distance * unit.distanceFactor
        return if (nonSI < 1) getShortDistanceDescription(distance) else f().decimal1.format(nonSI) + " " + unit.distanceUnit
    }

    private fun getShortDistanceDescription(value: Float): String {
        return f().decimal0.format(value * unit.altitudeFactor) + " " + unit.altitudeUnit
    }
}
