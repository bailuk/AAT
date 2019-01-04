package ch.bailu.aat.gpx.xml_parser.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.util.Objects;

public class Util {

    public static void skipTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        String tag = parser.getName();

        log(parser);
        while(tag != null) {

            int event = parser.next();

            if (event == XmlPullParser.END_TAG
                    && Objects.equals(tag, parser.getName())) {
                log(parser);
                return;
            }
        }
    }

    public static void log(XmlPullParser parser) throws XmlPullParserException {
        String event = "unknown";

        if (parser.getEventType() == XmlPullParser.END_TAG) event = "END_TAG";
        if (parser.getEventType() == XmlPullParser.START_TAG) event = "START_TAG";
        if (parser.getEventType() == XmlPullParser.END_DOCUMENT) event = "END_DOCUMENT";
        if (parser.getEventType() == XmlPullParser.START_DOCUMENT) event = "START_DOCUMENT";


        AppLog.d(parser,
                "E: " + event +
                        " N: " + parser.getName() +
                        " T: " + parser.getText() +
                        " A: " + parser.getAttributeCount());
    }
}
