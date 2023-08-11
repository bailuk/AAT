package ch.bailu.aat_lib.xml.parser.gpx

import ch.bailu.aat_lib.gpx.GpxConstants
import ch.bailu.aat_lib.util.Objects
import ch.bailu.aat_lib.xml.parser.osm.TagParser
import ch.bailu.aat_lib.xml.parser.parseAttributes
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

abstract class PntParser(t: String) : TagParser(t) {
    private val time: TagParser = TimeParser()
    private val ele: TagParser = EleParser()
    private val extensions: TagParser = ExtensionParser()
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {}

    @Throws(IOException::class)
    public override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {
        scanner.tags.clear()

        parser.parseAttributes { name, value ->
            if (Objects.equals(name, GpxConstants.QNAME_LATITUDE)) {
                scanner.latitude.scan(value)
            } else if (Objects.equals(name, GpxConstants.QNAME_LONGITUDE)) {
                scanner.longitude.scan(value)
            }
       }
    }

    @Throws(IOException::class, XmlPullParserException::class)
    public override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return ele.parse(parser, scanner) ||
                time.parse(parser, scanner) ||
                extensions.parse(parser, scanner)
    }
}
