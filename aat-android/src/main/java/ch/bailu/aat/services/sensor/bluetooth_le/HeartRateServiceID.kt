package ch.bailu.aat.services.sensor.bluetooth_le

open class HeartRateServiceID : ID() {
    companion object {
        private const val SERVICE_ID = 0x180d

        @JvmField
        val HEART_RATE_SERVICE = toUUID(SERVICE_ID)
        @JvmField
        val HEART_RATE_MEASUREMENT = toUUID(0x2a37)
        @JvmField
        val BODY_SENSOR_LOCATION = toUUID(0x2a38)
    }
}
