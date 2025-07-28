package ch.bailu.aat_lib.service.directory

import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import java.io.IOException

abstract class SummaryConfig {
    abstract fun getSummaryDir(dir: Foc): Foc
    abstract fun getDBPath(dir: Foc): String

    @Throws(IOException::class)
    fun getPreviewFile(gpxFile: Foc): Foc {
        if (!gpxFile.hasParent()) {
            throw IOException("File '$gpxFile' has no parent")
        }
        val name = gpxFile.name
        var dir = gpxFile.parent()
        dir = getSummaryDir(dir)
        return dir.child(name + AppDirectory.PREVIEW_EXTENSION)
    }
}
