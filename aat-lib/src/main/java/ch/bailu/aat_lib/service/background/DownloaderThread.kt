package ch.bailu.aat_lib.service.background

import ch.bailu.aat_lib.app.AppContext

class DownloaderThread(appContext: AppContext, private val server: String) : WorkerThread(
    "DT_$server", appContext, DOWNLOAD_QUEUE_SIZE
) {
    private val statistics = DownloadStatistics()

    override fun bgOnHandleProcessed(handle: BackgroundTask, size: Long) {
        totalSize += size

        if (size > 0) {
            statistics.success(size)
        } else {
            statistics.failure()
        }
    }

    override fun bgProcessHandle(handle: BackgroundTask) {
        if (statistics.isReady()) {
            super.bgProcessHandle(handle)
        }
    }

    fun appendStatusText(builder: StringBuilder) {
        statistics.appendStatusText(builder, server)
    }

    companion object {
        private const val DOWNLOAD_QUEUE_SIZE = 200
        var totalSize: Long = 0
            private set
    }
}
