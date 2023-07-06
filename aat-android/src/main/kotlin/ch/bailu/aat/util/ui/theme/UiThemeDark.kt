package ch.bailu.aat.util.ui.theme

import android.R
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ListView
import android.widget.TextView
import ch.bailu.aat_lib.app.AppColor

class UiThemeDark(private val hlColor: Int) : UiTheme {
    override fun getBackgroundColor(): Int {
        return Color.BLACK
    }

    override fun getHighlightColor(): Int {
        return hlColor
    }

    override fun getGraphBackgroundColor(): Int {
        return 0
    }

    override fun getGraphLineColor(): Int {
        return Color.DKGRAY
    }

    override fun getGraphTextColor(): Int {
        return Color.LTGRAY
    }

    override fun list(l: ListView) {
        val height = l.dividerHeight
        l.divider = ColorDrawable(hlColor)
        l.dividerHeight = height
        l.setSelector(R.color.transparent)
    }

    override fun background(v: View) {
        v.setBackgroundColor(Color.BLACK)
    }

    @Suppress("DEPRECATION")
    override fun button(v: View) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(0, hlColor))
    }

    override fun topic(v: TextView) {
        v.setTextColor(hlColor)
        v.textSize = UiTheme.HEADER_TEXT_SIZE
    }

    override fun header(v: TextView) {
        v.setTextColor(Color.WHITE)
        v.textSize = UiTheme.HEADER_TEXT_SIZE
    }

    override fun content(v: TextView) {
        v.setTextColor(Color.LTGRAY)
        v.setLinkTextColor(hlColor)
    }

    override fun toolTip(v: TextView) {
        v.setTextColor(AppColor.HL_BLUE)
    }

    override fun backgroundAlt(v: View) {
        v.setBackgroundColor(Color.DKGRAY)
    }

    override fun contentAlt(v: TextView) {
        v.setTextColor(Color.WHITE)
    }
}
