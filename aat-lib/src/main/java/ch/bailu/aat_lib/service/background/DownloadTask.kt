package ch.bailu.aat_lib.service.background

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.util.extensions.ellipsize
import ch.bailu.aat_lib.util.net.URX
import ch.bailu.foc.Foc
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


open class DownloadTask(
    val source: URX,
    target: Foc,
    private val downloadConnection: DownloadConfig

) : FileTask(target) {
    constructor(
        source: String,
        target: Foc,
        downloadConnection: DownloadConfig

    ) : this(URX(source), target, downloadConnection)

    override fun bgOnProcess(appContext: AppContext): Long {
        var size: Long = 0
        try {
            size = bgDownload()
            appContext.broadcaster.broadcast(
                AppBroadcaster.FILE_CHANGED_ONDISK, getFile().toString(), source.toString()
            )
        } catch (e: Exception) {
            logError(e)
            getFile().rm()
        }
        return size
    }

    @Throws(IOException::class)
    protected fun bgDownload(): Long {
        return download(source.toURL(), getFile())
    }

    protected open fun logError(e: Exception?) {
        AppLog.w(this, getFile().pathName)
        AppLog.w(this, e)
    }


    override fun toString(): String {
        return source.toString()
    }

    private fun download(url: URL, file: Foc): Long {
        var count: Int
        var total: Long = 0
        var input: InputStream? = null
        var output: OutputStream? = null
        val connection: HttpURLConnection
        val buffer = downloadConnection.createBuffer()

        try {
            output = file.openW()
            connection = downloadConnection.openConnection(url)
            input = downloadConnection.openInput(connection)
            while (input.read(buffer).also { count = it } != -1) {
                total += count.toLong()
                output.write(buffer, 0, count)
            }

        } catch (e: Exception) {
            AppLog.e(this, e, ToDo.translate("GET '${url.toString().ellipsize(50)}': failed" ))

        } finally {
            Foc.close(output)
            Foc.close(input)
        }
        return total
    }
}
