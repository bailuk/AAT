package ch.bailu.aat_lib.api

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.service.background.DownloadTask
import ch.bailu.aat_lib.util.fs.TextBackup
import ch.bailu.foc.Foc
import java.io.UnsupportedEncodingException

abstract class DownloadApi : OsmApiConfiguration() {
    private var task = BackgroundTask.Companion.NULL

    private class ApiQueryTask(c: AppContext, source: String, target: Foc, private val queryString: String, private val queryFile: Foc) : DownloadTask(source, target, c.downloadConfig) {

        override fun bgOnProcess(appContext: AppContext): Long {
            return try {
                val size = bgDownload()
                TextBackup.Companion.write(queryFile, queryString)
                appContext.broadcaster.broadcast(
                    AppBroadcaster.FILE_CHANGED_ONDISK, getFile().toString(), source.toString()
                )
                size
            } catch (e: Exception) {
                AppLog.e(this, e) // TODO user friendly message
                1
            }
        }
    }

    override fun startTask(appContext: AppContext, boundingBoxE6: BoundingBoxE6) {
        appContext.services.insideContext {
            try {
                val background = appContext.services.getBackgroundService()
                val query: String = queryString
                val url = getUrl(query, boundingBoxE6)
                task = ApiQueryTask(
                    appContext,
                    url,
                    resultFile,
                    query,
                    queryFile
                )
                background.process(task)
            } catch (e: UnsupportedEncodingException) {
                AppLog.e(this, e) // TODO user friendly message
            }
        }
    }

    protected abstract val queryString: String
}
