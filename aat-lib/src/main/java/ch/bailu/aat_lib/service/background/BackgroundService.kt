package ch.bailu.aat_lib.service.background

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.service.VirtualService
import ch.bailu.aat_lib.util.WithStatusText
import ch.bailu.foc.Foc

class BackgroundService(
    private val appContext: AppContext,
    broadcaster: Broadcaster?,
    threads: Int
) : VirtualService(), BackgroundServiceInterface, WithStatusText {
    private val tasks: Tasks = Tasks(broadcaster)
    private val downloaders = HashMap<String, DownloaderThread>(5)
    private val loaders = HashMap<String, LoaderThread>(5)
    private val queue: HandleStack = HandleStack()
    private val workers = ArrayList<WorkerThread>()

    init {

        for (i in 0 until threads) {
            workers.add(WorkerThread("WT_$i", appContext, queue))
        }
    }

    override fun process(backgroundTask: BackgroundTask) {
        if (backgroundTask is DownloadTask) {
            download(backgroundTask)
        } else if (backgroundTask is FileTask) {
            load(backgroundTask)
        } else {
            workers[0].process(backgroundTask)
        }
    }

    private fun download(handle: DownloadTask) {
        try {
            val url = handle.source.toURL()
            val host = url.host
            var downloader = downloaders[host]
            if (downloader == null) {
                downloader = DownloaderThread(appContext, host)
                downloaders[host] = downloader
            }
            handle.register(tasks)
            downloader.process(handle)
        } catch (e: Exception) {
            e(this, e)
        }
    }

    private fun load(handle: FileTask) {
        val base = getBaseDirectory(handle.file)
        var loader = loaders[base]
        if (loader == null) {
            loader = LoaderThread(appContext, base)
            loaders[base] = loader
        }
        handle.register(tasks)
        loader.process(handle)
    }

    override fun close() {
        for (p in loaders.values) p.close()
        loaders.clear()
        for (downloader in downloaders.values) downloader.close()
        downloaders.clear()
        for (w in workers) w.close()
        queue.close(workers.size)
    }

    private fun getBaseDirectory(file: Foc): String {
        var f = file
        var p = f.parent()
        var depth = 0
        while (p != null && depth < FILE_LOADER_BASE_DIRECTORY_DEPTH) {
            f = p
            p = p.parent()
            depth++
        }
        return f.pathName
    }

    override fun appendStatusText(builder: StringBuilder) {
        for (p in loaders.values) p.appendStatusText(builder)
        for (p in downloaders.values) p.appendStatusText(builder)
    }

    override fun findTask(file: Foc): FileTask? {
        return tasks[file]
    }

    companion object {
        const val FILE_LOADER_BASE_DIRECTORY_DEPTH = 4
    }
}
