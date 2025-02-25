package ch.bailu.aat_lib.service.background

import kotlin.math.max
import kotlin.math.min


class DownloadStatistics {
    private var failureRate = 0
    private var failure = 0L
    private var success = 0L
    private var bytes = 0L

    private var nextDownload = System.currentTimeMillis() - 100


    @Synchronized
    fun increment(size: Long) {
        bytes += size
    }

    @Synchronized
    fun failure() {
        failure++
        failureRate = min(MAX_RATE.toDouble(), (failureRate + 1).toDouble())
            .toInt()
        setNextDownloadTime()
    }

    @Synchronized
    fun success(size: Long) {
        increment(size)
        success()
    }

    @Synchronized
    fun success() {
        failureRate = max(0.0, (failureRate - 2).toDouble()).toInt()
        success++
        setNextDownloadTime()
    }

    private fun setNextDownloadTime() {
        nextDownload = System.currentTimeMillis() + (failureRate * failureRate * 1000) - 100
    }

    @Synchronized
    fun isReady(): Boolean {
        return System.currentTimeMillis() > nextDownload
    }

    @Synchronized
    fun appendStatusText(builder: StringBuilder, server: String) {
        val time = getBlockInterval()

        builder.append("<h2>")
        builder.append(server)
        builder.append("</h2>")
        builder.append("<p>Successfull downloads: ")
        builder.append(success)
        builder.append("<br>Total: ")
        builder.append(bytes)
        builder.append(" bytes")

        if (success > 0) {
            builder.append("<br>Average file pixelCount: ")
            builder.append(Math.round(bytes / success.toFloat()))
            builder.append(" bytes")
        }

        builder.append("<br>Failed downloads: ")
        builder.append(failure)

        builder.append("<br>Downloads blocked for ")
        builder.append(time)
        builder.append(" ms</p>")
    }

    @Synchronized
    fun getBlockInterval(): Long {
        return max(0.0, (nextDownload - System.currentTimeMillis()).toDouble())
            .toLong()
    }

    companion object {
        private const val MAX_RATE = 5
    }
}
