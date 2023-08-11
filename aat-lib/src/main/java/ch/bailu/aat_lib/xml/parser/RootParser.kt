package ch.bailu.aat_lib.xml.parser

import ch.bailu.aat_lib.xml.parser.gpx.GpxParser
import ch.bailu.aat_lib.xml.parser.osm.OsmParser
import ch.bailu.aat_lib.xml.parser.osm.SearchresultsParser
import ch.bailu.aat_lib.xml.parser.osm.TagParser
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class RootParser {
    val gpx: TagParser = GpxParser()
    val osm: TagParser = OsmParser()
    val nominatim: TagParser = SearchresultsParser()

    @Throws(IOException::class, XmlPullParserException::class)
    fun parse(parser: XmlPullParser, scanner: Scanner) {
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (!parseTags(parser, scanner)) {
                parser.skipTag()
            }
            parser.next()
        }
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return gpx.parse(parser, scanner) ||
                osm.parse(parser, scanner) ||
                nominatim.parse(parser, scanner)
    }
}
