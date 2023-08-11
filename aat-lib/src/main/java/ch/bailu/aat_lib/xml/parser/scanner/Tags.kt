package ch.bailu.aat_lib.xml.parser.scanner

import ch.bailu.aat_lib.gpx.attributes.GpxAttributesStatic
import ch.bailu.aat_lib.gpx.attributes.Keys.Companion.toIndex

class Tags {
    val list = ArrayList<GpxAttributesStatic.Tag>()
    fun clear() {
        list.clear()
    }

    fun notEmpty(): Boolean {
        return list.size > 0
    }

    fun sort() {
        list.sort()
    }

    fun add(k: String, v: String) {
        list.add(GpxAttributesStatic.Tag(toIndex(k), v))
    }

    fun get(): GpxAttributesStatic {
        return GpxAttributesStatic(list.toArray(arrayOf()))
    }
}
