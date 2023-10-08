package ch.bailu.aat_lib.preferences.system

import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.MemSize


class SolidCacheSize(storage: StorageInterface) : SolidIndexList(storage, KEY) {
    override fun length(): Int {
        return sizes.size
    }

    
    override fun getLabel(): String {
        return Res.str().p_cache_size()
    }

    override fun getValueAsString(index: Int): String {
        val b = StringBuilder()
        MemSize.describe(b, sizes[index])
        return toDefaultString(b.toString(), index)
    }

    val valueAsLong: Long
        get() = sizes[index]

    companion object {
        private const val MAX_CACHE_SIZE = 256 * MemSize.MB
        private const val ENTRIES = 11

        const val KEY = "cache_size"
        private var sizes: LongArray = LongArray(11).apply {
            val max = Math.min(Runtime.getRuntime().maxMemory(), MAX_CACHE_SIZE)
            sizes = LongArray(ENTRIES)
            sizes[0] = MemSize.round(max / 5)
            sizes[sizes.size - 1] = max
            for (i in sizes.size - 2 downTo 1) {
                sizes[i] = MemSize.round(sizes[i + 1] / 3 * 2)
            }
        }
    }
}
