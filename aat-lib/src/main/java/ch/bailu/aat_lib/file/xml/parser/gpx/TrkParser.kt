package ch.bailu.aat_lib.file.xml.parser.gpx

import ch.bailu.aat_lib.gpx.GpxConstants
import ch.bailu.aat_lib.file.xml.parser.osm.TagParser
import ch.bailu.aat_lib.file.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class TrkParser : TagParser(GpxConstants.QNAME_TRACK) {
    private val seg: TagParser = SegParser()
    private val pnt: TagParser = TrkPtParser()
    public override fun parseText(parser: XmlPullParser, scanner: Scanner) {}
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {}

    @Throws(IOException::class, XmlPullParserException::class)
    public override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return pnt.parse(parser, scanner) || seg.parse(parser, scanner)
    }

    public override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
