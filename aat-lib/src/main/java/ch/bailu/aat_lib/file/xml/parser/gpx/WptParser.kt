package ch.bailu.aat_lib.file.xml.parser.gpx

import ch.bailu.aat_lib.gpx.GpxConstants
import ch.bailu.aat_lib.file.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import java.io.IOException

class WptParser : PntParser(GpxConstants.QNAME_WAY_POINT) {
    @Throws(IOException::class)
    override fun parsed(parser: XmlPullParser, scanner: Scanner) {
        scanner.wayParsed.onHavePoint()
    }
}
