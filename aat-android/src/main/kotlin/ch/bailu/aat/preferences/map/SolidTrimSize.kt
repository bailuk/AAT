package ch.bailu.aat.preferences.map

import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.MemSize

/**
 * TODO: move to lib
 */
class SolidTrimSize(storageInterface: StorageInterface) : SolidIndexList(
    storageInterface, SolidTrimSize::class.java.simpleName
) {

    private class Entry(val value: Long) {
        val text: String = MemSize.describe(StringBuilder(), value)
    }

    override fun length(): Int {
        return entries.size
    }

    override fun getLabel(): String {
        return Res.str().p_trim_size()
    }

    fun getValue(): Long {
        return entries[index].value
    }

    public override fun getValueAsString(index: Int): String {
        return entries[index].text
    }

    companion object {
        private val entries = arrayOf(
            Entry(16L * MemSize.GB),
            Entry(8L * MemSize.GB),
            Entry(4L * MemSize.GB),
            Entry(2L * MemSize.GB),
            Entry(1L * MemSize.GB),
            Entry(500L * MemSize.MB),
            Entry(200L * MemSize.MB),
            Entry(100L * MemSize.MB),
            Entry(50L * MemSize.MB)
        )
    }
}
