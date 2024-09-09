package ch.bailu.aat_lib.service.background

import ch.bailu.aat_lib.app.AppContext

class LoaderThread(sc: AppContext, private val directory: String) : WorkerThread(
    "LT_$directory", sc, LOADER_QUEUE_SIZE
) {
    private var totalLoads = 0
    private var totalBytes: Long = 0


    override fun bgOnHandleProcessed(handle: BackgroundTask, size: Long) {
        totalLoads++
        totalBytes += size
    }

    fun appendStatusText(builder: StringBuilder) {
        builder.append("<h2>")
        builder.append(directory)
        builder.append("</h2>")
        builder.append("<p>Loads: ")
        builder.append(totalLoads)
        builder.append("<br>Total bytes: ")
        builder.append(totalBytes)
        builder.append(" bytes")

        if (totalLoads > 0) {
            builder.append("<br>Average bytes: ")
            builder.append(Math.round(totalBytes / totalLoads.toFloat()))
            builder.append(" bytes")
        }

        builder.append("</p>")
    }

    companion object {
        private const val LOADER_QUEUE_SIZE = 1000
    }
}
