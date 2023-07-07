package ch.bailu.aat.views.preferences

import android.content.Context
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.util.ui.tooltip.ToolTipView
import ch.bailu.aat_lib.preferences.SolidBoolean

class SolidCheckBox(context: Context?, sboolean: SolidBoolean, theme: UiTheme) :
    LinearLayout(context) {
    private val toolTip: ToolTipView

    init {
        orientation = VERTICAL
        val checkBox = CheckBox(getContext())
        theme.header(checkBox)
        addView(checkBox)
        toolTip = ToolTipView(getContext(), theme)
        toolTip.setToolTip(sboolean)
        addView(toolTip)
        checkBox.isChecked = sboolean.value
        checkBox.text = sboolean.getLabel()
        checkBox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            sboolean.value = isChecked
            toolTip.setToolTip(sboolean)
        }
    }
}
