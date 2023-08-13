package ch.bailu.aat.services.tileremover

import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc
import java.io.IOException

// TODO move to lib
class SourceSummaries {
    companion object {
        const val SUMMARY_SIZE = 20
        private val NULL_SUMMARY = SourceSummary("")
        private fun findInList(old: ArrayList<SourceSummary>, name: String): Int {
            for (i in old.indices) {
                if (old[i].name == name) return i
            }
            return -1
        }
    }

    private val sourceSummaries = ArrayList<SourceSummary>(SUMMARY_SIZE)

    init {
        reset()
    }

    private fun reset() {
        sourceSummaries.clear()
        sourceSummaries.add(SourceSummary(Res.str().p_trim_total()))
    }

    @Throws(IOException::class)
    fun rescanKeep(tileCacheDirectory: Foc) {
        val old = ArrayList(sourceSummaries)
        rescan(tileCacheDirectory)
        replaceFromList(old)
    }

    private fun replaceFromList(list: ArrayList<SourceSummary>) {
        for (index in 0 until size()) {
            val foundIndex = findInList(list, get(index).name)
            if (foundIndex > -1) {
                sourceSummaries[index] = list[foundIndex]
            }
        }
    }

    fun findIndex(name: String): Int {
        return findInList(sourceSummaries, name)
    }

    fun rescan(tileCacheDirectory: Foc) {
        reset()
        tileCacheDirectory.foreachDir { child: Foc -> sourceSummaries.add(SourceSummary(child.name)) }
    }

    fun addFile(file: TileFile) {
        val length = file.length()
        get(0).addFile(length)
        get(file.source).addFile(length)
    }

    fun resetToRemove() {
        for (summary in sourceSummaries) {
            summary.clearRm()
        }
    }

    fun addFileToRemove(f: TileFile) {
        val l = f.length()
        get(0).addFileToRemove(f.length())
        get(f.source).addFileToRemove(l)
    }

    fun addFileRemoved(f: TileFile) {
        val length = f.length()
        get(0).addFileRemoved(length)
        get(f.source).addFileRemoved(length)
    }

    operator fun get(index: Int): SourceSummary {
        return if (index < sourceSummaries.size) sourceSummaries[index] else NULL_SUMMARY
    }

    fun size(): Int {
        return sourceSummaries.size
    }

    fun toFile(baseDirectory: Foc, t: TileFile): Foc {
        return t.toFile(baseDirectory.child(get(t.source).name))
    }
}
