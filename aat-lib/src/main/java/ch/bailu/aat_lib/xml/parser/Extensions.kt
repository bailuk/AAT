package ch.bailu.aat_lib.xml.parser

import ch.bailu.aat_lib.util.Objects
import ch.bailu.aat_lib.util.Objects.equals
import ch.bailu.aat_lib.xml.parser.scanner.Scanner
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

fun Scanner.wayPointParsed() {
    if (this.tags.notEmpty()) {
        this.tags.sort()
        this.wayParsed.onHavePoint()
    }
}

/**
 * Read until tag ends or document ends
 *
 * @throws IOException
 * @throws XmlPullParserException
 */
fun XmlPullParser.skipTag() {
    val tag = this.name
    while (tag != null) {
        val event = this.next()
        if (event == XmlPullParser.END_TAG
            && equals(tag, this.name)
        ) {
            return
        } else if (event == XmlPullParser.END_DOCUMENT) {
            return
        }
    }

}


fun XmlPullParser.parseAttributes(parseAttribute: (name: String, value: String)->Unit) {
    for (i in 0 until this.attributeCount) {
        if (this.getAttributeName(i) != null && this.getAttributeValue(i) != null) {
            parseAttribute(Objects.toString(this.getAttributeName(i)), Objects.toString(this.getAttributeValue(i)))
        }
    }

}
