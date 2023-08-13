package ch.bailu.aat_lib.gpx.attributes

open class PowerAttributes(location: String, cadence: Boolean, speed: Boolean) : CadenceSpeedAttributes(location, cadence, speed
) {
    var power = 0
    override fun get(keyIndex: Int): String {
        return if (keyIndex == KEY_INDEX_POWER) power.toString() else super.get(
            keyIndex
        )
    }

    override fun getAsBoolean(keyIndex: Int): Boolean {
        return if (keyIndex == KEY_INDEX_CONTACT && power > 0) true else super.getAsBoolean(
            keyIndex
        )
    }

    override fun hasKey(keyIndex: Int): Boolean {
        return KEYS.hasKey(keyIndex)
    }

    override fun size(): Int {
        return KEYS.size()
    }

    override fun getAt(index: Int): String {
        return get(KEYS.getKeyIndex(index))
    }

    override fun getKeyAt(index: Int): Int {
        return KEYS.getKeyIndex(index)
    }

    companion object {
        val KEYS = Keys()

        init {
            /* copy all from CadenceSpeedAttributes.KEYS which this class
          supports as well (but we need a distinct Keys instance
          because adding "Power" to CadenceSpeedAttributes.KEYS would
          make that class appear to support "Power") */
            for (i in 0 until CadenceSpeedAttributes.KEYS.size()) KEYS.add(
                CadenceSpeedAttributes.KEYS.getKeyIndex(
                    i
                )
            )
        }

        @JvmField
        val KEY_INDEX_POWER = KEYS.add("Power")
    }
}
