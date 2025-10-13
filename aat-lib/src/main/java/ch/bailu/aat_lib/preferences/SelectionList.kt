package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.util.extensions.addUnique
import ch.bailu.foc.Foc

object SelectionList {
    fun addDr(dirs: ArrayList<Foc>, file: Foc) {
        if (file.canRead() && file.isDir) {
            dirs.addUnique(file)
        }
    }

    fun addRo(list: ArrayList<String>, file: Foc) {
        addRo(list, file, file)
    }

    fun addRo(list: ArrayList<String>, check: Foc, file: Foc) {
        if (check.canOnlyRead()) {
            list.addUnique(file.toString())
        }
    }

    fun addR(list: ArrayList<String>, file: Foc) {
        if (file.canRead()) list.addUnique(file.toString())
    }

    fun addSubdirectoriesR(list: ArrayList<String>, directory: Foc) {
        directory.foreachDir { child: Foc -> addR(list, child) }
    }

    fun addW(list: ArrayList<String>, file: Foc) {
        addW(list, file, file)
    }

    fun addW(list: ArrayList<String>, check: Foc, file: Foc) {
        if (check.canWrite()) {
            list.addUnique(file.toString())
        }
    }
}
