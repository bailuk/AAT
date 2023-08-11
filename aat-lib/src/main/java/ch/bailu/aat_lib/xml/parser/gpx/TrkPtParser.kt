package ch.bailu.aat_lib.xml.parser.gpx

import ch.bailu.aat_lib.gpx.GpxConstants
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import java.io.IOException

class TrkPtParser : PntParser(GpxConstants.QNAME_TRACK_POINT) {
    @Throws(IOException::class)
    override fun parsed(parser: XmlPullParser, scanner: Scanner) {
        scanner.trackParsed.onHavePoint()
    }
}
