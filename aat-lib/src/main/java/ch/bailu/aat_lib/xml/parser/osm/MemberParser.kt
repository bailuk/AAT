package ch.bailu.aat_lib.xml.parser.osm

import ch.bailu.aat_lib.util.Objects.equals
import ch.bailu.aat_lib.xml.parser.parseAttributes
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import java.io.IOException

open class MemberParser @JvmOverloads constructor(tag: String = "member") : TagParser(tag) {
    @Throws(IOException::class)
    override fun parseText(parser: XmlPullParser, scanner: Scanner) {
    }

    @Throws(IOException::class)
    override fun parseAttributes(parser: XmlPullParser, scanner: Scanner) {
        parser.parseAttributes { name, value ->
            if (equals(name, "ref")) {
                scanner.id.scan(value)
                val point = scanner.referencer[scanner.id.int]
                if (point != null) {
                    scanner.referencer.bounding.add(point)
                    scanner.referencer.resolved++
                }
            }
        }
    }

    override fun parseTags(parser: XmlPullParser, scanner: Scanner): Boolean {
        return false
    }

    override fun parsed(parser: XmlPullParser, scanner: Scanner) {}
}
