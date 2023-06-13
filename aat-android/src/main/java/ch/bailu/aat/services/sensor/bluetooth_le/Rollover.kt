package ch.bailu.aat.services.sensor.bluetooth_le

class Rollover {
    private var first = true
    private var previous: Long = 0
    private var current: Long = 0

    var delta = 0
        private set

    var total: Long = 0
        private set

    fun add(v: Long) {
        if (first) {
            first = false
            current = v
        }
        previous = current
        current = v
        delta = difference(current, previous)
        total += delta.toLong()
    }

    fun addUINT32(v: Long) {
        // reset instead of rollover (no documentation found on rollover)
        if (v < current) {
            previous = v
            current = v
        }
        add(v)
    }

    val isInitialized: Boolean
        get() = !first

    companion object {
        private const val MAX_UINT16 = 0xffff

        private fun difference(newer: Long, older: Long): Int {
            if (newer > older) return (newer - older).toInt() else if (older > newer) return (newer + MAX_UINT16 - older).toInt()
            return 0
        }
    }
}
