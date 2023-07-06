package ch.bailu.aat.util.ui.tooltip

import android.content.Context
import ch.bailu.aat_lib.util.ui.ToolTipProvider

class ToolTipRes(private val context: Context, private val res: Int) : ToolTipProvider {
    override fun getToolTip(): String {
        return context.resources.getString(res)
    }
}
