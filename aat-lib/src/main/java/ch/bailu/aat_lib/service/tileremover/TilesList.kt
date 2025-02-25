package ch.bailu.aat_lib.service.tileremover

import java.util.TreeSet

class TilesList {
    private val files = TreeSet { o1: TileFile, o2: TileFile ->
        o1.lastModified().compareTo(o2.lastModified())
    }

    private val filesToRemove = ArrayList<TileFile>(FILES_LIMIT)
    fun resetToRemove() {
        filesToRemove.clear()
    }

    fun add(file: TileFile) {
        files.add(file)
        if (files.size >= FILES_LIMIT) {
            files.pollLast()
        }
    }

    fun addToRemove(file: TileFile) {
        filesToRemove.add(file)
    }

    fun iteratorToRemove(): Iterator<TileFile> {
        return filesToRemove.iterator()
    }

    operator fun iterator(): Iterator<TileFile> {
        return files.iterator()
    }

    companion object {
        private const val FILES_LIMIT = 50000
    }
}
