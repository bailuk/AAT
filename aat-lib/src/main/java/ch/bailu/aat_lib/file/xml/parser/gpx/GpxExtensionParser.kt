package ch.bailu.aat_lib.file.xml.parser.gpx

import ch.bailu.aat_lib.file.xml.parser.osm.TagParser
import ch.bailu.aat_lib.file.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser

class GpxExtensionParser : TagParser() {
    private var key: String = ""

    override fun parseText(parser: XmlPullParser, scanner: Scanner) {
        val value = parser.text
        if (value != null && key.isNotEmpty()) {
            scanner.tags.add(key, value)
        }
    }

    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {
        key = parser.name
    }

    override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return false
    }

    override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
