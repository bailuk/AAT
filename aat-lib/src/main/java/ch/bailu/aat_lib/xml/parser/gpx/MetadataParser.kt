package ch.bailu.aat_lib.xml.parser.gpx

import ch.bailu.aat_lib.xml.parser.osm.TagParser
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class MetadataParser : TagParser("metadata") {
    private val time = TimeParser()
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {}
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {}

    @Throws(IOException::class, XmlPullParserException::class)
    public override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return time.parse(parser, scanner)
    }

    public override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
