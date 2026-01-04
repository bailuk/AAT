package ch.bailu.aat_lib.util.fs

import ch.bailu.foc.Foc

class FileNameSplitter(val file: Foc) {
    val prefix: String
    val extension: String
    val dotExtension: String

    init {
        val parts = file.name.split(".").filter { it.isNotEmpty() }

        if (parts.size > 1) {
            extension = parts[parts.size -1]
            dotExtension = ".$extension"
        } else {
            extension = ""
            dotExtension = ""
        }

        val name = parts.joinToString(".")
        val tmpPrefix = name.substring(0, name.length - dotExtension.length)

        prefix = tmpPrefix.ifEmpty {
            file.name
        }
    }
}
