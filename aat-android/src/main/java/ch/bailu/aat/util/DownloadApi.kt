package ch.bailu.aat.util

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.search.poi.OsmApiConfiguration
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.service.background.DownloadTask
import ch.bailu.foc.Foc
import java.io.UnsupportedEncodingException

abstract class DownloadApi : OsmApiConfiguration() {
    private var task = BackgroundTask.NULL

    private class ApiQueryTask(c: AppContext, source: String, target: Foc, private val queryString: String, private val queryFile: Foc) : DownloadTask(source, target, c.downloadConfig) {

        override fun bgOnProcess(sc: AppContext): Long {
            return try {
                val size = bgDownload()
                TextBackup.write(queryFile, queryString)
                sc.broadcaster.broadcast(
                    AppBroadcaster.FILE_CHANGED_ONDISK, file.toString(), source.toString()
                )
                size
            } catch (e: Exception) {
                exception = e
                1
            }
        }

        override fun logError(e: Exception) {
            AppLog.e(e)
        }
    }

    override fun startTask(appContext: AppContext) {
        appContext.services.insideContext {
            try {
                val background = appContext.services.backgroundService
                val query: String = queryString
                val url = getUrl(query)
                task = ApiQueryTask(
                    appContext,
                    url,
                    resultFile,
                    query,
                    queryFile
                )
                background.process(task)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
    }

    protected abstract val queryString: String

    override val exception: Exception?
        get() = task.exception
}
