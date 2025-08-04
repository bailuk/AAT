package ch.bailu.aat_lib.file.xml.parser.gpx

import ch.bailu.aat_lib.util.Objects.equals
import ch.bailu.aat_lib.file.xml.parser.osm.MemberParser
import ch.bailu.aat_lib.file.xml.parser.osm.NdParser
import ch.bailu.aat_lib.file.xml.parser.osm.OsmTagParser
import ch.bailu.aat_lib.file.xml.parser.osm.TagParser
import ch.bailu.aat_lib.file.xml.parser.parseAttributes
import ch.bailu.aat_lib.file.xml.parser.scanner.Scanner
import ch.bailu.aat_lib.file.xml.parser.wayPointParsed
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

open class WayParser @JvmOverloads constructor(tag: String = "way") : TagParser(tag) {

    private val tag: TagParser = OsmTagParser()
    private val member: TagParser = MemberParser()
    private val nd: TagParser = NdParser()
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {}

    @Throws(IOException::class)
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {
        scanner.tags.clear()
        scanner.referencer.clear()

        parser.parseAttributes { name, value ->
            if (equals(name, "id")) {
                scanner.id.scan(value)
                scanner.referencer.id = scanner.id.value
            }
        }
    }

    @Throws(IOException::class, XmlPullParserException::class)
    override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return tag.parse(parser, scanner) ||
                nd.parse(parser, scanner) ||
                member.parse(parser, scanner)
    }

    @Throws(IOException::class)
    override fun parsed(parser: XmlPullParser, scanner: Scanner) {
        if (scanner.referencer.resolved > 0) {
            rememberRelation(scanner)
            scanner.wayPointParsed()
        }
    }

    private fun rememberRelation(scanner: Scanner) {
        val b = scanner.referencer.bounding
        val c = b.center
        scanner.latitude.value = c.getLatitudeE6()
        scanner.longitude.value = c.getLongitudeE6()
        scanner.referencer.put(scanner.referencer.id, c)
    }
}
