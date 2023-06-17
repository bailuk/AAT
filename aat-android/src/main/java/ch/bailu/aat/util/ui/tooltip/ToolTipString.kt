package ch.bailu.aat.util.ui.tooltip

import ch.bailu.aat_lib.util.ui.ToolTipProvider

class ToolTipString(private val toolTip: String) : ToolTipProvider {
    override fun getToolTip(): String {
        return toolTip
    }
}
