package ch.bailu.aat.views.msg.overlay

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import ch.bailu.aat.util.AppIntent.getFile
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.foc_android.FocAndroid

class FileChangeMsgView(context: Context) : AbsBroadcastMsgView(
    context, AppBroadcaster.FILE_CHANGED_ONDISK
) {
    init {
        setSingleLine()
        ellipsize = TextUtils.TruncateAt.MIDDLE
    }

    override fun set(intent: Intent) {
        val file = FocAndroid.factory(context, getFile(intent))
        set(file.pathName)
    }
}
