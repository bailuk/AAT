package ch.bailu.aat.views.msg.overlay

import android.content.Context
import android.content.Intent
import ch.bailu.aat.util.AppIntent
import ch.bailu.aat.util.ui.tooltip.ToolTip.themeify
import ch.bailu.aat_lib.broadcaster.AppBroadcaster

class InfoLogMsgView(context: Context) : AbsBroadcastMsgView(context, AppBroadcaster.LOG_INFO) {
    init {
        themeify(this)
    }

    override fun set(intent: Intent) {
        val message = intent.getStringExtra(AppIntent.EXTRA_MESSAGE)
        if (message is String) {
            set(message)
        }
    }
}
