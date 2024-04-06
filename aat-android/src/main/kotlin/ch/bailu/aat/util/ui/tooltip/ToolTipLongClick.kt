package ch.bailu.aat.util.ui.tooltip

import android.view.View
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.util.ui.ToolTipProvider

class ToolTipLongClick(v: View, private val tip: ToolTipProvider) : View.OnLongClickListener {
    init {
        v.isLongClickable = true
        v.setOnLongClickListener(this)
    }

    override fun onLongClick(view: View): Boolean {
        val text = tip.getToolTip()
        if (!text.isNullOrEmpty()) {
            AppLog.i(view.context, text)
            return true
        }
        return false
    }
}
