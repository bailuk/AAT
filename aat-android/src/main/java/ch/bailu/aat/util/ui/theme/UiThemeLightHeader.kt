package ch.bailu.aat.util.ui.theme

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.TextView

class UiThemeLightHeader(private val hlColor: Int) : UiThemeLight() {
    override fun topic(v: TextView) {
        v.setTextColor(hlColor)
        v.textSize = UiTheme.HEADER_TEXT_SIZE * 1.5f
        v.setTypeface(null, Typeface.BOLD)
    }

    override fun header(v: TextView) {
        v.setTextColor(hlColor)
        v.textSize = UiTheme.HEADER_TEXT_SIZE
        v.setTypeface(null, Typeface.BOLD)
    }

    override fun getHighlightColor(): Int {
        return hlColor
    }

    override fun button(v: View) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(BUTTON_LIGHT_GRAY, BUTTON_GRAY))
    }

    companion object {
        const val BUTTON_GRAY = Color.GRAY
    }
}
