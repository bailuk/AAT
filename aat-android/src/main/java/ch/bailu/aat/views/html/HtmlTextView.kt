package ch.bailu.aat.views.html

import android.content.Context
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import ch.bailu.aat.util.AppHtml.fromHtml
import kotlin.math.roundToInt

open class HtmlTextView(context: Context) : TextView(context) {
    init {
        val p = textSize.roundToInt()
        setPadding(p, p, p, p)
        movementMethod = LinkMovementMethod.getInstance()
    }

    fun setHtmlText(text: String) {
        setText(fromHtml(text))
    }

    fun enableAutoLink() {
        autoLinkMask = Linkify.ALL
    }
}
