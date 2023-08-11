package ch.bailu.aat_lib.xml.parser.gpx

import ch.bailu.aat_lib.gpx.GpxConstants
import ch.bailu.aat_lib.xml.parser.osm.TagParser
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class GpxTpxExtension : TagParser(GpxConstants.QNAME_GPXTPX_EXTENSION) {
    private val gpxTag = GpxExtensionParser()
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {}
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {}

    @Throws(IOException::class, XmlPullParserException::class)
    override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return gpxTag.parse(parser, scanner)
    }

    override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
