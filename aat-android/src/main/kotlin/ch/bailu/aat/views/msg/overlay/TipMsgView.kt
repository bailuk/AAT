package ch.bailu.aat.views.msg.overlay

import android.content.Context
import ch.bailu.aat.util.ui.tooltip.ToolTip
import ch.bailu.aat.views.msg.AbsMsgView

class TipMsgView(context: Context) : AbsMsgView(context) {
    init {
        ToolTip.themeify(this)
    }

    override fun attach() {}
    override fun detach() {}
}
