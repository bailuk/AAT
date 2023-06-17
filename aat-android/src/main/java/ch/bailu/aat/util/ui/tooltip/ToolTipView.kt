package ch.bailu.aat.util.ui.tooltip

import android.content.Context
import android.widget.TextView
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.util.ui.ToolTipProvider

class ToolTipView(context: Context, theme: UiTheme) : TextView(context) {
    init {
        theme.toolTip(this)
        visibility = GONE
    }

    fun setToolTip(tip: ToolTipProvider) {
        val text = tip.toolTip
        visibility = if (text == null || text.isEmpty()) {
            GONE
        } else {
            setText(text)
            VISIBLE
        }
    }
}
