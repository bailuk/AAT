package ch.bailu.aat.util.ui.theme

import android.R
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.ListView
import android.widget.TextView

open class UiThemeLight : UiTheme {
    override fun getBackgroundColor(): Int {
        return Color.WHITE
    }

    override fun background(v: View) {
        v.setBackgroundColor(getBackgroundColor())
    }

    override fun backgroundAlt(v: View) {
        v.setBackgroundColor(BG_LIGHT_BLUE)
    }

    override fun button(v: View) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(BUTTON_LIGHT_GRAY, BUTTON_BLUE))
    }

    override fun topic(v: TextView) {
        v.setTextColor(HL_BLUE)
        v.textSize = UiTheme.HEADER_TEXT_SIZE * 1.5f
        v.setTypeface(null, Typeface.BOLD)
    }

    override fun header(v: TextView) {
        v.setTextColor(TXT_DARK_GRAY)
        v.textSize = UiTheme.HEADER_TEXT_SIZE
        v.setTypeface(null, Typeface.BOLD)
    }

    override fun content(v: TextView) {
        v.setTextColor(TXT_DARK_GRAY)
        v.setLinkTextColor(TXT_LINK_BLUE)
    }

    override fun contentAlt(v: TextView) {
        v.setTextColor(Color.WHITE)
    }

    override fun toolTip(v: TextView) {
        v.setTextColor(HL_BLUE)
    }

    override fun list(l: ListView) {
        l.dividerHeight = 0
        l.setSelector(R.color.transparent)
    }

    override fun getHighlightColor(): Int {
        return HL_BLUE
    }

    override fun getGraphBackgroundColor(): Int {
        return GRAPH_BG_DARK_GREEN
    }

    override fun getGraphLineColor(): Int {
        return Color.BLACK
    }

    override fun getGraphTextColor(): Int {
        return Color.WHITE
    }

    companion object {
        const val BG_CODE_LIGHT_GRAY = -0xc0c0d
        const val HL_BLUE = -0xec9028
        const val TXT_DARK_GRAY = -0xdbd6d2
        const val TXT_LINK_BLUE = -0xbd7320
        const val BG_LIGHT_BLUE = -0x735953
        const val BUTTON_BLUE = -0x77ec9028
        const val BUTTON_LIGHT_GRAY = -0x770c0c0d
        const val GRAPH_BG_DARK_GREEN = -0xb6a8a5
    }
}
