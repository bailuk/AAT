package ch.bailu.aat.services.sensor.bluetooth_le

open class CscServiceID : ID() {
    companion object {
        val CSC_SERVICE = toUUID(0x1816)
        val CSC_MEASUREMENT = toUUID(0x2A5B)
        const val BIT_SPEED = 0
        const val BIT_CADENCE = 1
        const val BIT_SPEED_AND_CADENCE = 2
        val CSC_FEATURE = toUUID(0x2A5C)
        val CSC_SENSOR_LOCATION = toUUID(0x2A5D)
        val CSC_CONTROL_POINT = toUUID(0x2A55)
    }
}
