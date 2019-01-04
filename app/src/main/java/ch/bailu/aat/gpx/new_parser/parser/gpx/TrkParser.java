package ch.bailu.aat.gpx.new_parser.parser.gpx;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.new_parser.parser.TagParser;
import ch.bailu.aat.gpx.new_parser.scanner.Scanner;

public class TrkParser extends TagParser {
    private final SegParser seg;
    private final PntParser pnt;


    public TrkParser(String prefix) {
        super(prefix);

        seg = new SegParser(prefix);
        pnt = new PntParser(prefix);
    }


    @Override
    public void parseText(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        char c = parser.getName().charAt(0);

        if (c=='t') {
            scanner.currentParsed=scanner.trackParsed;
        } else if (c=='w') {
            scanner.currentParsed=scanner.wayParsed;
        } else {
            scanner.currentParsed=scanner.routeParsed;
        }
    }

    @Override
    public boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return pnt.parse(parser, scanner) || seg.parse(parser, scanner);
    }

    @Override
    public void parsed(XmlPullParser parser, Scanner scanner) throws IOException {

    }
}
