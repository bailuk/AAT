package ch.bailu.aat.gpx.new_parser.parser.gpx;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.new_parser.parser.TagParser;
import ch.bailu.aat.gpx.new_parser.scanner.Scanner;

public class SegParser extends TagParser {
    private final PntParser pnt;

    public SegParser(String prefix) {
        super(prefix + "seg");

        pnt = new PntParser(prefix);
    }

    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

    }


    @Override
    public boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return pnt.parse(parser, scanner);

    }

    @Override
    public void parsed(XmlPullParser parser, Scanner scanner) throws IOException {
        scanner.currentParsed.onHaveSegment();
    }
}
