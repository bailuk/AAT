package ch.bailu.aat.views.layout

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import ch.bailu.aat.util.ui.theme.AppTheme.padding
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.util.ui.tooltip.ToolTipView
import ch.bailu.aat_lib.util.ui.ToolTipProvider

open class LabelTextView(
    inverse: Boolean, context: Context, labelText: String,
    theme: UiTheme
) : LinearLayout(context) {
    private val value: TextView
    private val label: TextView
    private val toolTip: ToolTipView
    private val inverse: Boolean

    constructor(context: Context, labelText: String, theme: UiTheme) : this(
        false,
        context,
        labelText,
        theme
    ) {
    }

    init {
        orientation = VERTICAL
        this.inverse = inverse
        label = TextView(context)
        label.text = labelText
        addView(label)
        value = TextView(context)
        addView(value)
        toolTip = ToolTipView(context, theme)
        addView(toolTip)
        padding(this)
        themify(theme)
    }

    fun setLabel(text: CharSequence) {
        label.text = text
    }

    fun setText(text: CharSequence) {
        value.text = text
    }

    fun setToolTip(tip: ToolTipProvider) {
        toolTip.setToolTip(tip)
    }

    fun setTextColor(color: Int) {
        label.setTextColor(color)
    }

    fun themify(theme: UiTheme) {
        if (inverse) {
            theme.header(value)
            theme.content(label)
        } else {
            theme.header(label)
            theme.content(value)
        }
    }
}
