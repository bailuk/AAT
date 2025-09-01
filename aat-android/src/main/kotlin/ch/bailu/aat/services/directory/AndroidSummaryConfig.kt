package ch.bailu.aat.services.directory

import android.content.Context
import ch.bailu.aat_lib.service.directory.SummaryConfig
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFile

class AndroidSummaryConfig(private val context: Context) : SummaryConfig() {
    override fun getSummaryDir(dir: Foc): Foc {
        val summaryDir: Foc = if (dir is FocFile && dir.mkdirs() && dir.canWrite()) {
            dir.child(AppDirectory.DIR_CACHE)
        } else {
            FocFile(context.cacheDir)
                .child("summary")
                .child(dir.hashCode().toString())
        }
        summaryDir.mkdirs()
        return summaryDir
    }

    override fun getDatabasePath(dir: Foc): String {
        return getSummaryDir(dir)
            .child(AppDirectory.FILE_CACHE_DB)
            .toString()
    }
}
