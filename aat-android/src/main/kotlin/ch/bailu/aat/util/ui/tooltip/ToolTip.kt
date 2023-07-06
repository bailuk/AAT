package ch.bailu.aat.util.ui.tooltip

import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.TextView
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat_lib.map.MapColor

object ToolTip {

    @JvmStatic
    fun set(view: View, resID: Int) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.tooltipText = ToolTipRes(view.context, resID).toolTip
        } else {
            ToolTipLongClick(view, ToolTipRes(view.context, resID))
        }
    }

    @JvmStatic
    fun set(view: View, toolTip: String) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.tooltipText = toolTip
        } else {
            ToolTipLongClick(view, ToolTipString(toolTip))
        }
    }

    @JvmStatic
    fun themeify(v: TextView) {
        v.setBackgroundColor(MapColor.LIGHT)
        v.setTextColor(Color.BLACK)
        AppTheme.padding(v, 10)
    }
}
