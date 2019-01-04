package ch.bailu.aat.gpx.xml_parser.parser.gpx;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxConstants;
import ch.bailu.aat.gpx.xml_parser.parser.TagParser;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;

public class GpxParser  extends TagParser {

    private TagParser metadata = new MetadataParser();
    private TagParser trk = new TrkParser();
    private TagParser rte = new RteParser();
    private TagParser wpt = new WptParser();


    public GpxParser() {
        super(GpxConstants.QNAME_GPX);
    }


    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) {

    }

    @Override
    public boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

        return
                metadata.parse(parser, scanner) ||
                trk.parse(parser, scanner) ||
                rte.parse(parser, scanner) ||
                wpt.parse(parser, scanner);
    }

    @Override
    public void parsed(XmlPullParser parser, Scanner scanner) {

    }
}


