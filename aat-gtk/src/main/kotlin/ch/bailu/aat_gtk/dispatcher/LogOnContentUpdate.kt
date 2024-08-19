package ch.bailu.aat_gtk.dispatcher

import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class LogOnContentUpdate(dispatcher: DispatcherInterface): OnContentUpdatedInterface {
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.ROOT)

    private val log = HashMap<Int, Data>()

    init {
        dispatcher.addTarget(this, InfoID.ALL)

        log[InfoID.TRACKER_TIMER] = Data(TimeOutLog(10000), "TRACKER_TIMER")
        log[InfoID.LOCATION] = Data(TimeOutLog(10000), "LOCATION")
        log[InfoID.TRACKER] = Data(TimeOutLog(10000),"TRACKER")
        log[InfoID.FILE_VIEW]= Data(TimeOutLog(), "FILEVIEW")
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {

        val data = log[iid]

        if (data is Data) {
            data.log.log {
                AppLog.d(this, "${dateFormat.format(System.currentTimeMillis())} ($it) ${data.name}: ${info.getFile()}")
            }
        } else {
            log[iid] = Data(TimeOutLog(), "UNKNOWN($iid)")
        }
    }

}

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
