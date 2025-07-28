package ch.bailu.aat_lib.file.xml.parser.gpx

import ch.bailu.aat_lib.file.xml.parser.osm.TagParser
import ch.bailu.aat_lib.file.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import java.io.IOException

class EleParser : TagParser("ele") {
    @Throws(IOException::class)
    public override fun parseText(parser: XmlPullParser, scanner: Scanner) {
        scanner.elevation.scan(parser.text)
    }

    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {}
    public override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return false
    }

    public override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
