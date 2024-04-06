package ch.bailu.aat_lib.preferences

import ch.bailu.foc.Foc

object SelectionList {
    fun addDr(dirs: ArrayList<Foc>, file: Foc) {
        if (file.canRead() && file.isDir) {
            dirs.add(file)
        }
    }

    fun addRo(list: ArrayList<String>, file: Foc) {
        addRo(list, file, file)
    }

    fun addRo(list: ArrayList<String>, check: Foc, file: Foc) {
        if (check.canOnlyRead()) {
            list.add(file.toString())
        }
    }

    fun addR(list: ArrayList<String>, file: Foc) {
        if (file.canRead()) list.add(file.toString())
    }

    fun addSubdirectoriesR(list: ArrayList<String>, directory: Foc) {
        directory.foreachDir { child: Foc -> addR(list, child) }
    }

    fun addW(list: ArrayList<String>, file: Foc) {
        addW(list, file, file)
    }

    fun addW(list: ArrayList<String>, check: Foc, file: Foc) {
        if (check.canWrite()) {
            list.add(file.toString())
        }
    }
}
