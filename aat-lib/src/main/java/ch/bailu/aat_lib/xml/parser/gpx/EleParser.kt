package ch.bailu.aat_lib.xml.parser.gpx

import ch.bailu.aat_lib.xml.parser.osm.TagParser
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import java.io.IOException

class EleParser : TagParser("ele") {
    @Throws(IOException::class)
    public override fun parseText(parser: XmlPullParser, scanner: Scanner) {
        scanner.altitude.scan(parser.text)
    }

    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {}
    public override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return false
    }

    public override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
