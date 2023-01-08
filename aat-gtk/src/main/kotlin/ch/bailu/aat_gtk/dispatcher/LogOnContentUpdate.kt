package ch.bailu.aat_gtk.dispatcher

import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog
import java.text.SimpleDateFormat
import java.util.*

class LogOnContentUpdate(dispatcher: Dispatcher): OnContentUpdatedInterface {
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.ROOT)

    private var countTimer = 0

    init {
        dispatcher.addTarget(this, InfoID.ALL)

    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {

        if (iid == InfoID.TRACKER_TIMER) {
            if (countTimer == 0) {
                countTimer = 100
            } else {
                countTimer--
                return
            }
        }
        AppLog.d(this, "${dateFormat.format(System.currentTimeMillis())} ${toInfoIdName(iid)}: ${info.file}")

    }

    private fun toInfoIdName(iid: Int): String {
        return when (iid) {
            4 -> {
                "TRACKER_TIMER"
            }
            3 -> {
                "TRACKER"
            }
            1 -> {
                "LOCATION"
            }
            else -> iid.toString()
        }
    }
}
