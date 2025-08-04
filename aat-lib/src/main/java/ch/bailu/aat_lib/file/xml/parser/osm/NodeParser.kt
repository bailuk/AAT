package ch.bailu.aat_lib.file.xml.parser.osm

import ch.bailu.aat_lib.coordinates.LatLongE6
import ch.bailu.aat_lib.util.Objects.equals
import ch.bailu.aat_lib.file.xml.parser.parseAttributes
import ch.bailu.aat_lib.file.xml.parser.scanner.Scanner
import ch.bailu.aat_lib.file.xml.parser.wayPointParsed
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class NodeParser : TagParser("node") {
    private val tag: TagParser = OsmTagParser()
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {}
    @Throws(IOException::class)
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {
        scanner.tags.clear()

        parser.parseAttributes {name, value ->
            if (equals(name, "id")) {
                scanner.id.scan(value)
            } else if (equals(name, "lat")) {
                scanner.latitude.scan(value)
            } else if (equals(name, "lon")) {
                scanner.longitude.scan(value)
            }
        }
    }

    @Throws(IOException::class, XmlPullParserException::class)
    override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return tag.parse(parser, scanner)
    }

    @Throws(IOException::class)
    override fun parsed(parser: XmlPullParser, scanner: Scanner) {
        scanner.referencer.put(
            scanner.id.value,
            LatLongE6(scanner.latitude.value, scanner.longitude.value)
        )
        scanner.wayPointParsed()
    }
}
