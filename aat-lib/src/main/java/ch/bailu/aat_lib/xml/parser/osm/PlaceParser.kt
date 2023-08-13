package ch.bailu.aat_lib.xml.parser.osm

import ch.bailu.aat_lib.util.Objects.equals
import ch.bailu.aat_lib.xml.parser.parseAttributes
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import ch.bailu.aat_lib.xml.parser.wayPointParsed
import org.xmlpull.v1.XmlPullParser
import java.io.IOException

class PlaceParser : TagParser("place") {
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {}
    @Throws(IOException::class)
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {
        scanner.tags.clear()

        parser.parseAttributes { name, value ->
            if (equals(name, "lat")) {
                scanner.latitude.scan(value)
            } else if (equals(name, "lon")) {
                scanner.longitude.scan(value)
            } else {
                scanner.tags.add(name, value)
            }
        }
    }

    override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return false
    }

    @Throws(IOException::class)
    override fun parsed(parser: XmlPullParser, scanner: Scanner) {
        scanner.wayPointParsed()
    }
}
