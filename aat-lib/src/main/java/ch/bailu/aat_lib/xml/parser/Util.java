package ch.bailu.aat_lib.xml.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat_lib.xml.parser.scanner.Scanner;
import ch.bailu.util.Objects;

public class Util {

    /**
     * Read until tag ends or document ends
     *
     * @param parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static void skipTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        final String tag = parser.getName();

        while (tag != null) {

            int event = parser.next();

            if (event == XmlPullParser.END_TAG
                    && Objects.equals(tag, parser.getName())) {
                return;

            } else if (event == XmlPullParser.END_DOCUMENT) {
                return;

            }
        }
    }


    public static void wayPointParsed(Scanner scanner) throws IOException {
        if (scanner.tags.notEmpty()) {
            scanner.tags.sort();
            scanner.wayParsed.onHavePoint();
        }
    }
}
