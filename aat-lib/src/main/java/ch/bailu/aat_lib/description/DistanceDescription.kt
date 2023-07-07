package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.description.FF.Companion.f
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.resources.Res
import java.util.Objects

open class DistanceDescription(storage: StorageInterface) : FloatDescription() {
    private val format = arrayOf(f().N3, f().N2, f().N1, f().N)
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
        setCache(info.distance)
    }

    fun getDistanceDescription(distance: Float): String {
        val nonSI = distance * unit.distanceFactor
        return if (nonSI < 1) getAltitudeDescription(distance.toDouble()) else f().N.format(nonSI.toDouble()) + " " + unit.distanceUnit
    }

    fun getDistanceDescriptionN1(distance: Float): String {
        val nonSI = distance * unit.distanceFactor
        return if (nonSI < 1) getAltitudeDescription(distance.toDouble()) else f().N1.format(nonSI.toDouble()) + " " + unit.distanceUnit
    }

    fun getAltitudeDescription(value: Double): String {
        return f().N.format(
            value * unit.altitudeFactor
        ) +
                unit.altitudeUnit
    }
}
