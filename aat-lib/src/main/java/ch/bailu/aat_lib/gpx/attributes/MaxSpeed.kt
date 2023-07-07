package ch.bailu.aat_lib.gpx.attributes

import ch.bailu.aat_lib.gpx.GpxPointNode

abstract class MaxSpeed : GpxSubAttributes(KEYS) {
    abstract fun get(): Float
    abstract fun add(speed: Float)
    override fun update(point: GpxPointNode, autoPause: Boolean): Boolean {
        if (!autoPause) add(point.getSpeed())
        return autoPause
    }

    override fun get(keyIndex: Int): String {
        return if (keyIndex == INDEX_MAX_SPEED) {
            get().toString()
        } else NULL_VALUE
    }

    override fun getAsFloat(keyIndex: Int): Float {
        return if (keyIndex == INDEX_MAX_SPEED) get() else super.getAsFloat(keyIndex)
    }

    class Raw2 : MaxSpeed() {
        private var maximum = 0f
        override fun get(): Float {
            return maximum
        }

        override fun add(speed: Float) {
            maximum = Math.max(speed, maximum)
        }
    }

    class Samples @JvmOverloads constructor(samples: Int = 5) : MaxSpeed() {
        private val speeds: FloatArray
        private var i = 0
        private var maximum = 0f

        init {
            speeds = FloatArray(Math.max(samples, 1))
        }

        override fun get(): Float {
            return maximum
        }

        override fun add(speed: Float) {
            insert(speed)
            set()
        }

        private fun set() {
            maximum = Math.max(maximum, smallest)
        }

        private fun insert(speed: Float) {
            speeds[i] = speed
            i = ++i % speeds.size
        }

        private val smallest: Float
            get() {
                var r = speeds[0]
                for (i in 1 until speeds.size) {
                    r = Math.min(r, speeds[i])
                }
                return r
            }
    }

    companion object {
        private val KEYS = Keys()
        @JvmField
        val INDEX_MAX_SPEED = KEYS.add("MaxSpeed")
    }
}
