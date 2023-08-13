package ch.bailu.aat_lib.gpx.attributes

import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.AltitudeDelta.LastAverage
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed.Raw2
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed.Samples
import ch.bailu.aat_lib.gpx.attributes.SampleRate.StepsRate
import ch.bailu.aat_lib.preferences.SolidAutopause

class GpxListAttributes(vararg attr: GpxSubAttributes) :
    GpxSubAttributes(keysFromSubAttributes(attr)) {
    private val attributes: Array<out GpxSubAttributes>

    init {
        attributes = attr
    }

    override fun get(keyIndex: Int): String {
        for (attr in attributes) {
            if (attr.hasKey(keyIndex)) return attr[keyIndex]
        }
        return NULL_VALUE
    }

    override fun getAsFloat(keyIndex: Int): Float {
        for (attr in attributes) {
            if (attr.hasKey(keyIndex)) return attr.getAsFloat(keyIndex)
        }
        return super.getAsFloat(keyIndex)
    }

    override fun getAsLong(keyIndex: Int): Long {
        for (attr in attributes) {
            if (attr.hasKey(keyIndex)) return attr.getAsLong(keyIndex)
        }
        return super.getAsLong(keyIndex)
    }

    override fun getAsInteger(keyIndex: Int): Int {
        for (attr in attributes) {
            if (attr.hasKey(keyIndex)) return attr.getAsInteger(keyIndex)
        }
        return super.getAsInteger(keyIndex)
    }

    fun update(p: GpxPointNode) {
        update(p, false)
    }

    override fun update(point: GpxPointNode, autoPause: Boolean): Boolean {
        var result = autoPause
        for (attr in attributes) {
            result = result || attr.update(point, result)
        }
        return result
    }

    companion object {
        @JvmField
        val NULL = GpxListAttributes()

        private fun keysFromSubAttributes(attr: Array<out GpxSubAttributes>): Keys {
            val keys = Keys()
            for (a in attr) {
                for (i in 0 until a.size()) {
                    keys.add(a.getKeyAt(i))
                }
            }
            return keys
        }

        @JvmStatic
        fun factoryTrackList(): GpxListAttributes {
            return GpxListAttributes(Raw2())
        }

        @JvmStatic
        fun factoryTrack(spause: SolidAutopause): GpxListAttributes {
            return factoryTrack(
                AutoPause.Time(
                    spause.triggerSpeed,
                    spause.triggerLevelMillis
                )
            )
        }


        @JvmStatic
        fun factoryTrack(autoPause: AutoPause): GpxListAttributes {
            return GpxListAttributes(
                Samples(),
                autoPause,
                LastAverage(),
                SampleRate.Cadence(),
                SampleRate.HeartRate(),
                StepsRate(),
                Steps()
            )
        }

        @JvmStatic
        fun factoryRoute(): GpxListAttributes {
            return GpxListAttributes(LastAverage())
        }
    }
}
