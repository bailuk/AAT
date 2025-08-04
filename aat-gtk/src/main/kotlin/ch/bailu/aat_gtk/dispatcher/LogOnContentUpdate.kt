package ch.bailu.aat_gtk.dispatcher

import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.logger.AppLog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

private data class Data(val log: TimeOutLog, val name: String)

private class TimeOutLog(private val timeout: Long = 0) {
    private var lastLog: Long = 0
    private var missed = 0

    fun log(cb: (missed: Int)->Unit) {
        val time = System.currentTimeMillis()

        if (time - lastLog > timeout) {
            cb(missed)
            lastLog = time
            missed = 0
        } else {
            missed++
        }
    }
}
