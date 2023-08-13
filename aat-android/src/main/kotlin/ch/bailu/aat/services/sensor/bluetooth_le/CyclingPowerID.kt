package ch.bailu.aat.services.sensor.bluetooth_le

/**
 * Definitions for the Cycling Power GATT service.
 * [...](https://www.bluetooth.com/specifications/specs/cycling-power-service-1-1/)
 * [...](https://www.bluetooth.com/specifications/specs/cycling-power-profile-1-1/)
 * [...](https://www.bluetooth.com/specifications/assigned-numbers/)
 */
open class CyclingPowerID : ID() {
    companion object {
        @JvmField
        val CYCLING_POWER_SERVICE = toUUID(0x1818)

        @JvmField
        val SENSOR_LOCATION = toUUID(0x2A5D)

        @JvmField
        val CYCLING_POWER_MEASUREMENT = toUUID(0x2a63)
        val CYCLING_POWER_VECTOR = toUUID(0x2a64)

        @JvmField
        val CYCLING_POWER_FEATURE = toUUID(0x2a65)

        val CYCLING_POWER_CONTROL_POINT = toUUID(0x2a66)

        const val FEATURE_BIT_WHEEL_REVOLUTIONS = 2
        const val FEATURE_BIT_CRANK_REVOLUTIONS = 3
        const val BIT_PEDAL_POWER_BALANCE = 0
        const val BIT_PEDAL_POWER_BALANCE_REFERENCE = 1
        const val BIT_ACCUMULATED_TORQUE = 2
        const val BIT_ACCUMULATED_TORQUE_SOURCE = 3
        const val BIT_WHEEL_REVOLUTION_DATA = 4
        const val BIT_CRANK_REVOLUTION_DATA = 5
        const val BIT_EXTREME_FORCE_MAGNITUDES = 6
        const val BIT_EXTREME_TORQUE_MAGNITUDES = 7
        const val BIT_EXTREME_ANGLES = 8
        const val BIT_TOP_DEAD_SPOT_ANGLE = 9
        const val BIT_BOTTOM_DEAD_SPOT_ANGLE = 10
        const val BIT_ACCUMULATED_ENERGY = 11
        const val BIT_OFFSET_COMPENSATION_INDICATOR = 12
    }
}
