package ch.bailu.aat.views.msg.permission

import android.content.Context
import android.widget.TextView
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme

open class PermissionInfoView(context: Context, theme: UiTheme) : TextView(context) {
    init {
        theme.background(this)
        theme.toolTip(this)
        AppTheme.padding(this, 5)
    }

    fun updateText(text: String) {
        visibility = if (text.isEmpty()) {
            GONE
        } else {
            VISIBLE
        }
        this.text = text
    }
}
