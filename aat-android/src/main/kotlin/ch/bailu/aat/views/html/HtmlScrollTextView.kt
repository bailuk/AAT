package ch.bailu.aat.views.html

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.preferences.VerticalScrollView

class HtmlScrollTextView(context: Context, text: String = "") : VerticalScrollView(context) {
    val textView: HtmlTextView = HtmlTextView(context)

    init {
        add(textView)
        setHtmlText(text)
        textView.setTextIsSelectable(true)
    }

    fun setHtmlText(text: String) {
        textView.setHtmlText(text)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        textView.setOnClickListener(listener)
        super.setOnClickListener(listener)
    }

    fun themify(theme: UiTheme) {
        theme.content(textView)
    }
}
