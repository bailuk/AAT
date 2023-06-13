package ch.bailu.aat.services.sensor.bluetooth_le

import java.util.UUID

open class ID {
    companion object {
        const val MINUTE = 60 * 1024

        @JvmStatic
        fun isBitSet(b: Byte, bit: Int): Boolean {
            return b.toInt() and (1 shl bit) != 0
        }

        private fun toIDString(id: Int): String {
            return Integer.toHexString(id)
        }

        @JvmStatic
        fun toUUID(id: Int): UUID {
            return UUID.fromString("0000" + toIDString(id) + "-0000-1000-8000-00805f9b34fb")
        }
    }
}
