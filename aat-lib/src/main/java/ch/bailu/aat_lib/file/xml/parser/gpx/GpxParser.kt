package ch.bailu.aat_lib.file.xml.parser.gpx

import ch.bailu.aat_lib.gpx.GpxConstants
import ch.bailu.aat_lib.file.xml.parser.osm.TagParser
import ch.bailu.aat_lib.file.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class GpxParser : TagParser(GpxConstants.QNAME_GPX) {
    private val metadata: TagParser = MetadataParser()
    private val trk: TagParser = TrkParser()
    private val rte: TagParser = RteParser()
    private val wpt: TagParser = WptParser()

    // for non standard GPX files
    private val seg: TagParser = SegParser()

    @Throws(IOException::class)
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {
    }

    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {}
    @Throws(IOException::class, XmlPullParserException::class)
    public override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return metadata.parse(parser, scanner) ||
                trk.parse(parser, scanner) ||
                rte.parse(parser, scanner) ||
                wpt.parse(parser, scanner) ||
                seg.parse(parser, scanner)
    }

    public override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
