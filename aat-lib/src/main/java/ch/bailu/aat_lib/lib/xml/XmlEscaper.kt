package ch.bailu.aat_lib.lib.xml


class XmlEscaper {
    private val sb = StringBuilder()

    fun escape(input: String): String {
        sb.setLength(0)
        for (c in input.toCharArray()) {
            if (isInvalidXmlChar(c)) {
                continue
            }

            when (c) {
                '&' -> sb.append("&amp;")
                '<' -> sb.append("&lt;")
                '>' -> sb.append("&gt;")
                '"' -> sb.append("&quot;")
                '\'' -> sb.append("&apos;")
                else -> sb.append(c)
            }
        }
        return sb.toString()
    }

    private fun isInvalidXmlChar(c: Char): Boolean {
        // ASCII control characters and DEL
        return (c.code < 32 || c.code == 127)
    }

}
