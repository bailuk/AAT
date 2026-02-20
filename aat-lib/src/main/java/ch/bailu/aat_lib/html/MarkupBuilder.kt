package ch.bailu.aat_lib.html

import com.google.common.html.HtmlEscapers


open class MarkupBuilder(protected val config: MarkupConfig) {
    private val stringBuilder = StringBuilder()
    private val htmlEscaper = HtmlEscapers.htmlEscaper()

    fun clear() {
        stringBuilder.setLength(0)
    }

    fun appendHeader(s: String) {
        stringBuilder.append(config.bigOpen)
        appendEscaped(s)
        stringBuilder.append(config.bigClose)
    }

    fun append(s: String) {
        appendEscaped(s)
    }

    fun appendBold(s: String) {
        stringBuilder.append(config.boldOpen)
        appendEscaped(s)
        stringBuilder.append(config.boldClose)
    }


    fun appendNl() {
        stringBuilder.append(config.newLine)
    }

    fun appendBold(k: String, v: String) {
        stringBuilder.append(config.boldOpen)
        appendEscaped(k)
        stringBuilder.append("=")
        appendEscaped(v)
        stringBuilder.append(config.boldClose)
    }

    fun appendKeyValue(k: String, v: String) {
        appendEscaped(k)
        stringBuilder.append("=")
        appendEscaped(v)
    }

    fun append(l: String, v: String) {
        appendEscaped(l)
        stringBuilder.append(": ")
        appendEscaped(v)
    }

    private fun appendEscaped(s: String) {
        stringBuilder.append(htmlEscaper.escape(s))
    }

    override fun toString(): String {
        return stringBuilder.toString()
    }
}
