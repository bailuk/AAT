package ch.bailu.aat_lib.xml.parser.osm

import ch.bailu.aat_lib.xml.parser.gpx.WayParser
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class OsmParser : TagParser("osm") {
    private val meta: TagParser = MetaParser()
    private val node: TagParser = NodeParser()
    private val relation: TagParser = RelationParser()
    private val way: TagParser = WayParser()
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {}
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {}

    @Throws(IOException::class, XmlPullParserException::class)
    override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return node.parse(parser, scanner) ||
                relation.parse(parser, scanner) ||
                way.parse(parser, scanner) ||
                meta.parse(parser, scanner)
    }

    override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
