package ch.bailu.aat_lib.service.directory

import ch.bailu.foc.Foc
import java.util.NavigableMap
import java.util.TreeMap

class FilesInDirectory(directory: Foc) {
    private val files: NavigableMap<String, Foc> = getFileList(directory)

    fun findItem(name: String): Foc? {
        return files[name]
    }

    fun pollItem(file: Foc): Foc? {
        return files.remove(file.name)
    }

    fun pollItem(): Foc? {
        return files.pollLastEntry()?.value
    }

    val size: Int
        get() = files.size

    companion object {
        private fun getFileList(directory: Foc): NavigableMap<String, Foc> {
            val files: NavigableMap<String, Foc> = TreeMap()
            directory.foreachFile { child: Foc -> files[child.name] = child }
            return files
        }
    }
}
