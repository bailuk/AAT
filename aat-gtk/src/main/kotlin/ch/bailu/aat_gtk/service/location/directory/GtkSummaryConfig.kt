package ch.bailu.aat_gtk.service.location.directory

import ch.bailu.aat_lib.service.directory.SummaryConfig
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc

class GtkSummaryConfig : SummaryConfig() {
    override fun getSummaryDir(dir: Foc): Foc {
        return dir.child(AppDirectory.DIR_CACHE)
    }

    override fun getDBPath(dir: Foc): String {
        return getSummaryDir(dir)
                .child(AppDirectory.FILE_CACHE_MVDB)
                .toString()
    }
}