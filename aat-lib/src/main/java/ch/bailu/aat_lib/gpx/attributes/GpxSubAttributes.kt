package ch.bailu.aat_lib.gpx.attributes

import ch.bailu.aat_lib.gpx.GpxPointNode

abstract class GpxSubAttributes(private val keys: Keys) : GpxAttributes() {
    abstract fun update(point: GpxPointNode, autoPause: Boolean): Boolean
    override fun size(): Int {
        return keys.size()
    }

    override fun getAt(index: Int): String {
        return get(keys.getKeyIndex(index))
    }

    override fun getKeyAt(index: Int): Int {
        return keys.getKeyIndex(index)
    }

    override fun hasKey(keyIndex: Int): Boolean {
        return keys.hasKey(keyIndex)
    }
}
