package ch.bailu.aat.gpx.xml_parser.parser.gpx;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxConstants;
import ch.bailu.aat.gpx.xml_parser.parser.TagParser;
import ch.bailu.aat.gpx.xml_parser.parser.osm.GpxExtensionParser;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;

public class GpxTpxExtension extends TagParser {

    private final GpxExtensionParser gpxTag = new GpxExtensionParser();

    public GpxTpxExtension() {
        super(GpxConstants.QNAME_GPXTPX_EXTENSION);
    }

    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

    }

    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return gpxTag.parse(parser, scanner);
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) throws IOException {

    }
}

