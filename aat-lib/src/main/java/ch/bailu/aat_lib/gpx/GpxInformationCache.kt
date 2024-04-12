package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.util.IndexedMap

class GpxInformationCache : OnContentUpdatedInterface {

    private val cache = IndexedMap<Int, GpxInformation>()

    fun forEach(cb: (value: GpxInformation) -> Unit) {
        cache.forEach { _, info -> cb(info) }
    }

    fun get(iid: Int): GpxInformation {
        return getValueAt(indexOf(iid))
    }

    fun getValueAt(index: Int): GpxInformation {
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
        if (info.isLoaded() && info.getGpxList().pointList.size() > 0) {
            cache.put(iid, info)
        } else if (isInCache(iid)) {
            cache.remove(iid)
        }
    }

    private fun indexOf(iid: Int): Int {
        return cache.indexOfKey(iid)
    }

    fun size(): Int {
        return cache.size()
    }

    fun getKeyAt(index: Int): Int {
        var result = InfoID.UNSPECIFIED
        try {
            cache.getKeyAt(index)?.apply {
                result = this
            }
        } catch (e: IndexOutOfBoundsException) {
            AppLog.w(this, e)
        }
        return result
    }
}
