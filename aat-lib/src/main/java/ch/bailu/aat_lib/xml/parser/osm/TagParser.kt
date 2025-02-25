package ch.bailu.aat_lib.xml.parser.osm

import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import ch.bailu.aat_lib.xml.parser.skipTag
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

abstract class TagParser(private val tag: String = "") {

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(parser: XmlPullParser, scanner: Scanner): Boolean {
        if (begins(parser)) {
            var event = START_MY_TAG
            do {
                if (event == START_MY_TAG) {
                    parseAttributes(parser, scanner)
                } else if (event == XmlPullParser.TEXT && parser.text != null) {
                    parseText(parser, scanner)
                } else if (event == XmlPullParser.START_TAG) {
                    if (!parseTags(parser, scanner)) {
                        parser.skipTag()
                    }
                }
                event = parser.next()
            } while (!ends(parser))
            parsed(parser, scanner)
            return true
        }
        return false
    }

    @Throws(IOException::class)
    protected abstract fun parseText(parser: XmlPullParser, scanner: Scanner)

    @Throws(IOException::class)
    protected abstract fun parseAttributes(parser: XmlPullParser, scanner: Scanner)

    @Throws(IOException::class, XmlPullParserException::class)
    protected abstract fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean

    @Throws(IOException::class)
    protected abstract fun parsed(parser: XmlPullParser, scanner: Scanner)

    @Throws(XmlPullParserException::class)
    private fun begins(parser: XmlPullParser): Boolean {
        if (parser.eventType == XmlPullParser.START_TAG) {
            return tag.isEmpty() || parser.name == tag
        }
        return false
    }

    @Throws(XmlPullParserException::class)
    private fun ends(parser: XmlPullParser): Boolean {
        if (parser.eventType == XmlPullParser.END_DOCUMENT || parser.eventType == XmlPullParser.END_TAG) {
            return tag.isEmpty() || parser.name == tag
        }
        return false
    }

    companion object {
        private const val START_MY_TAG = 9999
    }
}
