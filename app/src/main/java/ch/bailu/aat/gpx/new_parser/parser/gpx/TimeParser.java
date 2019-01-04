package ch.bailu.aat.gpx.new_parser.parser.gpx;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.new_parser.parser.TagParser;
import ch.bailu.aat.gpx.new_parser.scanner.Scanner;

public class TimeParser extends TagParser {

    public TimeParser() {
        super("time");
    }

    @Override
    public void parseText(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        scanner.dateTime.scan(parser.getText());
    }


    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

    }

    @Override
    public boolean parseTags(XmlPullParser parser, Scanner scanner) {
        return false;
    }

    @Override
    public void parsed(XmlPullParser parser, Scanner scanner) throws IOException {

    }
}
