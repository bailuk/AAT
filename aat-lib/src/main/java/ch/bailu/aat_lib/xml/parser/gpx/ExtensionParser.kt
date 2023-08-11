package ch.bailu.aat_lib.xml.parser.gpx;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat_lib.gpx.GpxConstants;
import ch.bailu.aat_lib.xml.parser.osm.TagParser;
import ch.bailu.aat_lib.xml.parser.osm.OsmTagParser;
import ch.bailu.aat_lib.xml.parser.scanner.Scanner;

public class ExtensionParser extends TagParser {
    private final GpxTpxExtension gpxtpx = new GpxTpxExtension();
    private final TagParser tag = new OsmTagParser();
    private final GpxExtensionParser gpxTag = new GpxExtensionParser();

    public ExtensionParser() {
        super(GpxConstants.QNAME_EXTENSIONS);
    }

    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) {

    }

    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return tag.parse(parser, scanner) || gpxtpx.parse(parser, scanner) || gpxTag.parse(parser, scanner);
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) {

    }
}
