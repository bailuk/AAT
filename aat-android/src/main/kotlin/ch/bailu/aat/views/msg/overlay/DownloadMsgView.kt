package ch.bailu.aat.views.msg.overlay

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import ch.bailu.aat.util.AppIntent.getUrl
import ch.bailu.aat_lib.dispatcher.AppBroadcaster

class DownloadMsgView(context: Context) :
    AbsBroadcastMsgView(context, AppBroadcaster.FILE_CHANGED_ONDISK) {
    init {
        setTextColor(Color.WHITE)
        setSingleLine()
        ellipsize = TextUtils.TruncateAt.MIDDLE
    }

    override fun set(intent: Intent) {
        val url = getUrl(intent)
        if (url != null && url.startsWith("http")) {
            set(url)
        }
    }
}
