package ch.bailu.aat.gpx.xml_parser.parser.gpx;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.xml_parser.parser.TagParser;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;

public class EleParser extends TagParser {
    public EleParser() {
        super("ele");
    }

    @Override
    public void parseText(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        scanner.altitude.scan(parser.getText());
    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) {

    }

    @Override
    public boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return false;
    }

    @Override
    public void parsed(XmlPullParser parser, Scanner scanner) {

    }
}
