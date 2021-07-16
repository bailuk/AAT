package ch.bailu.aat.gpx.xml_parser.parser.gpx;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxConstants;
import ch.bailu.aat.gpx.xml_parser.parser.TagParser;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;

public class SegParser extends TagParser {
    private final TagParser trkpt = new TrkptParser();

    public SegParser() {
        super(GpxConstants.QNAME_TRACK_SEGMENT);
    }

    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) {

    }


    @Override
    public boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return trkpt.parse(parser, scanner);

    }

    @Override
    public void parsed(XmlPullParser parser, Scanner scanner) {
        scanner.trackParsed.onHaveSegment();
    }
}
