package ch.bailu.aat_lib.xml.parser.osm

import ch.bailu.aat_lib.util.Objects.equals
import ch.bailu.aat_lib.xml.parser.parseAttributes
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import java.io.IOException

class OsmTagParser : TagParser(OsmConstants.T_TAG) {
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {}
    @Throws(IOException::class)
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {
        val k = arrayOf<String?>("")
        val v = arrayOf<String?>("")

        parser.parseAttributes { name, value ->
            if (equals(name, OsmConstants.A_KEY)) {
                k[0] = value
            } else if (equals(name, OsmConstants.A_VALUE)) {
                v[0] = value
            }
        }
        scanner.tags.add(k[0], v[0])
    }

    override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return false
    }

    override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
