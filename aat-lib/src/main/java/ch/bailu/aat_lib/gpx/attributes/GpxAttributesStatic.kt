package ch.bailu.aat_lib.gpx.attributes

class GpxAttributesStatic(private var tagList: Array<Tag> = arrayOf()) :
    GpxAttributes() {
    class Tag(val key: Int, val value: String) : Comparable<Tag> {
        override fun compareTo( other: Tag): Int {
            return key.compareTo(other.key)
        }
    }

    override fun get(keyIndex: Int): String {
        val index = getIndex(keyIndex)
        return if (index == size()) NULL_VALUE else tagList[index].value
    }

    override fun hasKey(keyIndex: Int): Boolean {
        return getIndex(keyIndex) < size()
    }

    override fun put(keyIndex: Int, value: String) {
        val index = getIndex(keyIndex)
        val tag = Tag(keyIndex, value)

        if (index == size()) {
            tagList = arrayOf(*tagList, tag)
        } else {
            tagList[index] = tag
        }
    }

    private fun getIndex(key: Int): Int {
        for (i in 0 until size()) {
            if (tagList[i].key == key) return i
        }
        return size()
    }

    override fun size(): Int {
        return tagList.size
    }

    override fun getAt(index: Int): String {
        return tagList[index].value
    }

    override fun getKeyAt(index: Int): Int {
        return tagList[index].key
    }
}
