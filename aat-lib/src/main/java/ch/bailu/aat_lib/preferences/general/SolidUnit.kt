package ch.bailu.aat_lib.preferences.general

import ch.bailu.aat_lib.preferences.SolidStaticIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import java.util.Objects


class SolidUnit(storage: StorageInterface) : SolidStaticIndexList(storage, KEY, Res.str().p_unit_list()) {
    init {
        Objects.requireNonNull(storage)
    }

    val distanceFactor: Float
        get() = DIST_FACTOR[index]
    val altitudeFactor: Float
        get() = ALT_FACTOR[index]
    val speedFactor: Float
        get() = SPEED_FACTOR[index]
    val distanceUnit: String
        get() = DIST_UNIT[index]
    val altitudeUnit: String
        get() = ALT_UNIT[index]
    val speedUnit: String
        get() = SPEED_UNIT[index]

    
    override fun getLabel(): String {
        return Res.str().p_unit_title()
    }

    val paceUnit: String
        get() = PACE_UNIT[index]
    val paceFactor: Float
        get() = 1f / distanceFactor

    companion object {
        private const val KEY = "unit"
        const val IMPERIAL = 1
        const val SI = 3
        private val DIST_FACTOR = floatArrayOf(1f / 1000f, 0.000621371f, 1f, 1f)
        val ALT_FACTOR = floatArrayOf(1f, 3.28084f, 1f, 1f)
        private val SPEED_FACTOR = floatArrayOf(3.6f, 2.23694f, 1f, 1f)
        private val DIST_UNIT = arrayOf("km", "miles", "m", "m")
        private val SPEED_UNIT = arrayOf("km/h", "mph", "m/s", "m/s")
        val ALT_UNIT = arrayOf("m", "f", "m", "m")
        val PACE_UNIT = arrayOf("T/km", "T/miles", "T/m", "s/m")
    }
}
