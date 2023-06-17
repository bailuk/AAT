package ch.bailu.aat.util

import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.Html.ImageGetter
import android.text.Html.TagHandler
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.TypefaceSpan
import ch.bailu.aat.util.ui.theme.UiThemeLight
import org.xml.sax.XMLReader

object AppHtml {
    @JvmOverloads
    @JvmStatic
    fun fromHtml(source: String, imageGetter: ImageGetter? = null): Spanned {
        return if (Build.VERSION.SDK_INT >= 24) {
            val flags =
                Html.FROM_HTML_MODE_LEGACY or Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM
            Html.fromHtml(source, flags, imageGetter, CODE_TAG_HANDLER)
        } else {
            Html.fromHtml(source, imageGetter, CODE_TAG_HANDLER)
        }
    }

    private val CODE_TAG_HANDLER: TagHandler = object : TagHandler {
        private var code = 0
        override fun handleTag(
            opening: Boolean,
            tag: String,
            output: Editable,
            xmlReader: XMLReader
        ) {
            if (tag == "code") {
                if (opening) {
                    code = output.length
                } else {
                    output.setSpan(
                        BackgroundColorSpan(UiThemeLight.BG_CODE_LIGHT_GRAY),
                        code, output.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    output.setSpan(
                        TypefaceSpan("monospace"),
                        code,
                        output.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }
    }
}
