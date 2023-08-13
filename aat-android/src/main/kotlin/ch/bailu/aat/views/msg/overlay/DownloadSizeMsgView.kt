package ch.bailu.aat.views.msg.overlay

import android.content.Context
import android.content.Intent
import android.graphics.Color
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.service.background.DownloaderThread
import ch.bailu.aat_lib.util.MemSize

class DownloadSizeMsgView(context: Context) : AbsBroadcastMsgView(
    context, AppBroadcaster.FILE_CHANGED_ONDISK
) {
    private var size: Long = 0
    val builder = StringBuilder()

    init {
        setTextColor(Color.WHITE)
    }

    override fun set(intent: Intent) {
        val newSize = DownloaderThread.getTotalSize()
        if (size != newSize) {
            size = newSize
            builder.setLength(0)
            MemSize.describe(builder, size.toDouble())
            set(builder.toString())
        }
    }
}
