package ch.bailu.aat.services.sensor.bluetooth_le

class Revolution {
    private val time = Rollover()
    private val revolution = Rollover()
    fun addUINT32(t: Int, r: Long) {
        time.add(t.toLong())
        revolution.addUINT32(r)
    }

    fun add(t: Int, r: Int) {
        time.add(t.toLong())
        revolution.add(r.toLong())
    }

    fun rpm(): Int {
        var rpm = 0
        val timeForOneRevolution = interval()
        if (timeForOneRevolution > 0) rpm = ID.MINUTE / timeForOneRevolution
        return rpm
    }

    private fun interval(): Int {
        return if (revolution.delta > 0) time.delta / revolution.delta else 0
    }

    fun getSpeedSI(circumferenceSI: Float): Float {
        if (circumferenceSI > 0) {
            val time1024 = interval()
            if (time1024 > 0) {
                return circumferenceSI * 1024f / time1024
            }
        }
        return 0f
    }

    val totalRevolutions: Long
        get() = revolution.total
    val isInitialized: Boolean
        get() = time.isInitialized && revolution.isInitialized
}
