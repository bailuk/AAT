package ch.bailu.aat_gtk.dispatcher

import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.util.IndexedMap
import java.lang.IndexOutOfBoundsException

class InfoCache : OnContentUpdatedInterface {

    private val cache = IndexedMap<Int, GpxInformation>()

    fun forEach(cb: (value: GpxInformation) -> Unit) {
        cache.forEach { _, info -> cb(info) }
    }

    fun get(iid: Int): GpxInformation {
        return getValueAt(indexOf(iid))
    }

    private fun getValueAt(index: Int): GpxInformation {
        val info = cache.getValueAt(index)
        if (info is GpxInformation) {
            return info
        }
        return GpxInformation.NULL
    }

    fun isInCache(iid: Int): Boolean {
        return cache.indexOfKey(iid) > -1
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (info.getLoaded() && info.getGpxList().pointList.size() > 0) {
            cache.put(iid, info)
        } else if (isInCache(iid)) {
            cache.remove(iid)
        }
    }

    private fun indexOf(iid: Int): Int {
        return cache.indexOfKey(iid)
    }

    fun withKeyAt(index: Int, cb: (iid: Int) -> Unit) {
        try {
            val iid = cache.getKeyAt(index)
            if (iid is Int) {
                cb(iid)
            }
        } catch (e: IndexOutOfBoundsException) {}
    }
}
