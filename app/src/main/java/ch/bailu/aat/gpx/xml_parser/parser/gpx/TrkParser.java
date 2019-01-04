package ch.bailu.aat.gpx.xml_parser.parser.gpx;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxConstants;
import ch.bailu.aat.gpx.xml_parser.parser.TagParser;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;

public class TrkParser extends TagParser {
    private final TagParser seg = new SegParser();
    private final TagParser pnt = new TrkptParser();


    public TrkParser() {
        super(GpxConstants.QNAME_TRACK);
    }


    @Override
    public void parseText(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
    }

    @Override
    public boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return pnt.parse(parser, scanner) || seg.parse(parser, scanner);
    }

    @Override
    public void parsed(XmlPullParser parser, Scanner scanner) throws IOException {

    }
}
