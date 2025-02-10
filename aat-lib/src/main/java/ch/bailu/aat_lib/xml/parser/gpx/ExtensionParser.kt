package ch.bailu.aat_lib.xml.parser.gpx

import ch.bailu.aat_lib.gpx.GpxConstants
import ch.bailu.aat_lib.xml.parser.osm.OsmTagParser
import ch.bailu.aat_lib.xml.parser.osm.TagParser
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class ExtensionParser : TagParser(GpxConstants.QNAME_EXTENSIONS) {
    private val gpxtpx = GpxTpxExtension()
    private val tag: TagParser = OsmTagParser()
    private val gpxTag = GpxExtensionParser()
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {}
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {}

    @Throws(IOException::class, XmlPullParserException::class)
    override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return tag.parse(parser, scanner) ||
                gpxtpx.parse(parser, scanner) ||
                gpxTag.parse(parser, scanner)
    }

    override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
