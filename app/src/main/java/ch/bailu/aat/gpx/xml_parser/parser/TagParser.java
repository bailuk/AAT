package ch.bailu.aat.gpx.xml_parser.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;

public abstract class TagParser {

    private final static int START_MY_TAG = 9999;

    private final String tag;


    public TagParser(String t) {
        tag = t;
    }


    public boolean parse(XmlPullParser parser, Scanner scanner) throws XmlPullParserException, IOException {

        if (begins(parser)) {

            int event = START_MY_TAG;

            do {
                if (event == START_MY_TAG) {
                    parseAttributes(parser, scanner);

                } else if (event == XmlPullParser.TEXT && parser.getText() != null) {
                    parseText(parser, scanner);

                } else if (event == XmlPullParser.START_TAG) {
                    if (parseTags(parser, scanner) == false) {
                        Util.skipTag(parser);
                    }
                }

                event = parser.next();
            } while(!ends(parser));

            parsed(parser, scanner);
            return true;
        }
        return false;
    }




    protected abstract void parseText(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException;
    protected abstract void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException;
    protected abstract boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException;
    protected abstract void parsed(XmlPullParser parser, Scanner scanner) throws IOException;


    private boolean begins(XmlPullParser parser) throws XmlPullParserException {
        return parser.getEventType() == XmlPullParser.START_TAG
                && parser.getName().equals(tag);
    }

    private boolean ends(XmlPullParser parser) throws XmlPullParserException {
        return parser.getEventType() == XmlPullParser.END_TAG
                && parser.getName().equals(tag);
    }



}
