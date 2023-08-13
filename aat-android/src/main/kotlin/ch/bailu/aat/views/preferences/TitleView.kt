package ch.bailu.aat.views.preferences

import android.content.Context
import android.widget.TextView
import ch.bailu.aat.util.ui.theme.UiTheme

class TitleView(context: Context, text: CharSequence, theme: UiTheme) : TextView(context) {
    constructor(context: Context, res: Int, theme: UiTheme) : this(context, context.getText(res), theme)

    init {
        setText(text)
        theme.topic(this)
    }
}
