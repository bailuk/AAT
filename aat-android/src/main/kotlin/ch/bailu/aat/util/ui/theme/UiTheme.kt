package ch.bailu.aat.util.ui.theme

import android.view.View
import android.widget.ListView
import android.widget.TextView

interface UiTheme {
    fun background(v: View)
    fun backgroundAlt(v: View)
    fun button(v: View)
    fun topic(v: TextView)
    fun header(v: TextView)
    fun content(v: TextView)
    fun contentAlt(v: TextView)
    fun toolTip(v: TextView)
    fun getBackgroundColor(): Int
    fun getHighlightColor(): Int
    fun getGraphBackgroundColor(): Int
    fun getGraphLineColor(): Int
    fun getGraphTextColor(): Int
    fun list(l: ListView)

    companion object {
        const val HEADER_TEXT_SIZE = 18f
    }
}
