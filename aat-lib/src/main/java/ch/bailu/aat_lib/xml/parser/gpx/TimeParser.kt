package ch.bailu.aat_lib.xml.parser.gpx

import ch.bailu.aat_lib.gpx.GpxConstants
import ch.bailu.aat_lib.xml.parser.osm.TagParser
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import java.io.IOException

class TimeParser : TagParser(GpxConstants.QNAME_TIME) {
    @Throws(IOException::class)
    public override fun parseText(parser: XmlPullParser, scanner: Scanner) {
        scanner.dateTime.scan(parser.text)
    }

    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {}
    public override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return false
    }

    public override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
