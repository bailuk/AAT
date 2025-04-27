package ch.bailu.aat_lib.file.xml.parser.osm

import ch.bailu.aat_lib.util.Objects.equals
import ch.bailu.aat_lib.file.xml.parser.parseAttributes
import ch.bailu.aat_lib.file.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import java.io.IOException

class OsmTagParser : TagParser(_root_ide_package_.ch.bailu.aat_lib.file.xml.parser.osm.OsmConstants.T_TAG) {
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {}

    @Throws(IOException::class)
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {
        var k = ""
        var v = ""

        parser.parseAttributes { name, value ->
            if (equals(name, _root_ide_package_.ch.bailu.aat_lib.file.xml.parser.osm.OsmConstants.A_KEY)) {
                k = value
            } else if (equals(name, _root_ide_package_.ch.bailu.aat_lib.file.xml.parser.osm.OsmConstants.A_VALUE)) {
                v = value
            }
        }
        scanner.tags.add(k, v)
    }

    override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return false
    }

    override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
