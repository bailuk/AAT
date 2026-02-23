package ch.bailu.aat_lib.gpx.attributes

/** Empty [GpxAttributes] that has no keys and returns [NULL_VALUE] for all lookups. */
class GpxAttributesNull : GpxAttributes() {
    override fun get(keyIndex: Int): String {
        return NULL_VALUE
    }

    override fun hasKey(keyIndex: Int): Boolean {
        return false
    }

    override fun size(): Int {
        return 0
    }

    override fun getAt(index: Int): String {
        return NULL_VALUE
    }

    override fun getKeyAt(index: Int): Int {
        return 0
    }

    companion object {
        @JvmField
        val NULL: GpxAttributes = GpxAttributesNull()
    }
}
