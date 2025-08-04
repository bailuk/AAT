package ch.bailu.aat_lib.file.xml.parser.osm

import ch.bailu.aat_lib.file.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class SearchResultsParser : TagParser("searchresults") {
    private val place: TagParser = PlaceParser()
    @Throws(IOException::class)
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {
    }

    @Throws(IOException::class)
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {
    }

    @Throws(IOException::class, XmlPullParserException::class)
    override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return place.parse(parser, scanner)
    }

    override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
