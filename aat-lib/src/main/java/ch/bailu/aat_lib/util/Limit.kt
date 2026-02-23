package ch.bailu.aat_lib.util

import kotlin.math.max
import kotlin.math.min

object Limit {
    @JvmStatic
    fun clamp(value: Int, min: Int, max: Int): Int {
        var result = value
        result = max(result, min)
        result = min(result, max)
        return result
    }

    @JvmStatic
    fun smallest(value: Int, vararg values: Int): Int {
        var result = value
        for (v in values) {
            result = min(result, v)
        }
        return result
    }

    @JvmStatic
    fun biggest(value: Int, vararg values: Int): Int {
        var result = value
        for (v in values) {
            result = max(result, v)
        }
        return result
    }

    @JvmStatic
    fun smallest(value: Double, vararg values: Double): Double {
        var value = value
        for (v in values) {
            value = min(value, v)
        }
        return value
    }

    @JvmStatic
    fun biggest(value: Double, vararg values: Double): Double {
        var value = value
        for (v in values) {
            value = max(value, v)
        }
        return value
    }
}
