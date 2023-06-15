package ch.bailu.aat.services.sensor.internal

import kotlin.math.pow

class Hypsometric {
    var pressureAtSeaLevel = 0.0
    private var pressure = 0.0

    fun setPressure(d: Double) {
        pressure = d
    }

    var altitude: Double
        get() = if (isValid) {
            getAltitude(pressure, pressureAtSeaLevel)
        } else 0.0

        set(a) {
            if (isPressureValid) {
                pressureAtSeaLevel = getPressureAtSeaLevel(pressure, a)
            }
        }

    private val isPressureValid: Boolean
        get() = pressure > MIN_PRESSURE

    val isPressureAtSeaLevelValid: Boolean
        get() = pressureAtSeaLevel > MIN_PRESSURE

    private val isValid: Boolean
        get() = isPressureValid && isPressureAtSeaLevelValid

    companion object {
        /**
         * See
         * https://physics.stackexchange.com/questions/333475/how-to-calculate-altitude-from-current-temperature-and-pressure
         */
        private const val MIN_PRESSURE = 100.0
        private const val TEMPERATURE_CELSIUS = 15.0
        private const val TEMPERATURE_KELVIN = TEMPERATURE_CELSIUS + 273.15
        private const val Ex = 5.257
        private const val Rx = 1.0 / Ex
        private const val Ax = 0.0065
        fun getAltitude(pressure: Double, pressureAtSeaLevel: Double): Double {
            val px = pressureAtSeaLevel / pressure
            return (px.pow(Rx) - 1.0) * TEMPERATURE_KELVIN / Ax
        }

        fun getPressureAtSeaLevel(pressure: Double, altitude: Double): Double {
            val hx = altitude * Ax
            return pressure * (hx / TEMPERATURE_KELVIN + 1.0).pow(Ex)
        }
    }
}
