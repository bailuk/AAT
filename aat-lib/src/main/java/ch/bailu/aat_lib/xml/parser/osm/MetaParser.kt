package ch.bailu.aat_lib.xml.parser.osm

import ch.bailu.aat_lib.util.Objects.equals
import ch.bailu.aat_lib.xml.parser.parseAttributes
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import java.io.IOException

class MetaParser : TagParser("meta") {
    @Throws(IOException::class)
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {
    }

    @Throws(IOException::class)
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {
        parser.parseAttributes{name, value ->
            if (equals(name, "osm_base")) {
                scanner.dateTime.scan(value)
            }
        }
    }

    override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return false
    }

    override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
