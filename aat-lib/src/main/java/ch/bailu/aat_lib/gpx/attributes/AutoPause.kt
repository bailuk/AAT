package ch.bailu.aat_lib.gpx.attributes

import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.interfaces.GpxDeltaInterface

abstract class AutoPause : GpxSubAttributes(KEYS) {
    abstract fun getPauseTime(): Long
    abstract fun getPauseDistance(): Float
    abstract fun update(delta: GpxDeltaInterface): Boolean
    override fun update(point: GpxPointNode, autoPause: Boolean): Boolean {
        return !update(point)
    }

    override fun get(keyIndex: Int): String {
        if (keyIndex == INDEX_AUTO_PAUSE_TIME) {
            return getPauseTime().toString()
        } else if (keyIndex == INDEX_AUTO_PAUSE_DISTANCE) {
            return getPauseDistance().toString()
        }
        return NULL_VALUE
    }

    override fun getAsFloat(keyIndex: Int): Float {
        return if (keyIndex == INDEX_AUTO_PAUSE_DISTANCE) {
            getPauseDistance()
        } else super.getAsFloat(keyIndex)
    }

    override fun getAsLong(keyIndex: Int): Long {
        return if (keyIndex == INDEX_AUTO_PAUSE_TIME) {
            getPauseTime()
        } else super.getAsLong(keyIndex)
    }

    class Time(private val minSpeed: Float, private val minTime: Int) : AutoPause() {
        private var addTime: Long = 0
        private var addDistance = 0f
        private var pauseTime: Long = 0
        private var pauseDistance = 0f
        override fun getPauseDistance(): Float {
            return if (addTime < minTime) pauseDistance else pauseDistance + addDistance
        }

        override fun getPauseTime(): Long {
            return if (addTime < minTime) pauseTime else pauseTime + addTime
        }

        override fun update(delta: GpxDeltaInterface): Boolean {
            if (delta.getSpeed() < minSpeed) {
                addTime += delta.getTimeDelta()
                addDistance += delta.getDistance()
            } else {
                if (addTime > minTime) {
                    pauseTime += addTime
                    pauseDistance += addDistance
                }
                addTime = 0
                addDistance = 0f
            }
            return addTime <= minTime // not paused
        }
    }

    companion object {
        private val KEYS = Keys()
        val INDEX_AUTO_PAUSE_TIME = KEYS.add("AutoPause")
        val INDEX_AUTO_PAUSE_DISTANCE = KEYS.add("AutoPauseDistance")
        @JvmField
        val NULL: AutoPause = object : AutoPause() {
            override fun getPauseTime(): Long {
                return 0
            }

            override fun getPauseDistance(): Float {
                return 0f
            }

            override fun update(delta: GpxDeltaInterface): Boolean {
                return true
            }
        }
    }
}
