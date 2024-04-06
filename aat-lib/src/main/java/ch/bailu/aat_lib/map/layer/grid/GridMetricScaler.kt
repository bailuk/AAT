package ch.bailu.aat_lib.map.layer.grid

import kotlin.math.roundToInt

class GridMetricScaler {
    var optimalScale = 0
        private set

    fun findOptimalScale(distance: Int) {
        val fdistance = (distance / 2).toFloat()
        for (level in GRID_LEVELS) {
            val flevel = level.toFloat()
            if ((fdistance / flevel).roundToInt() > 0) {
                optimalScale = level
                break
            }
        }
    }

    companion object {
        private const val KM = 1000
        private val GRID_LEVELS = intArrayOf(
            500 * KM,
            200 * KM,
            100 * KM,
            50 * KM,
            20 * KM,
            10 * KM,
            5 * KM,
            2 * KM,
            1 * KM,
            500,
            200,
            100,
            50,
            20,
            10,
            5,
            2
        )
    }
}
