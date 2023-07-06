package ch.bailu.aat.views.html

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.preferences.VerticalScrollView

class HtmlScrollTextView(context: Context) : VerticalScrollView(context) {
    val textView: HtmlTextView

    init {
        textView = HtmlTextView(context)
        add(textView)
    }

    constructor(context: Context, text: String) : this(context) {
        setHtmlText(text)
    }

    fun setHtmlText(text: String) {
        textView.setHtmlText(text)
    }

    fun enableAutoLink() {
        textView.enableAutoLink()
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        textView.setOnClickListener(listener)
        super.setOnClickListener(listener)
    }

    fun themify(theme: UiTheme) {
        theme.content(textView)
    }
}
