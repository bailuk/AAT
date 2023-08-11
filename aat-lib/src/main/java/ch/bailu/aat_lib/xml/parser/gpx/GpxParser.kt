package ch.bailu.aat_lib.xml.parser.gpx;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat_lib.gpx.GpxConstants;
import ch.bailu.aat_lib.xml.parser.osm.TagParser;
import ch.bailu.aat_lib.xml.parser.scanner.Scanner;

public class GpxParser  extends TagParser {

    private final TagParser metadata = new MetadataParser();
    private final TagParser trk = new TrkParser();
    private final TagParser rte = new RteParser();
    private final TagParser wpt = new WptParser();

    // for non standard GPX files
    private final TagParser seg = new SegParser();


    public GpxParser() {
        super(GpxConstants.QNAME_GPX);
    }


    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) throws IOException {

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
                        wpt.parse(parser, scanner) ||
                        seg.parse(parser, scanner);
    }

    @Override
    public void parsed(XmlPullParser parser, Scanner scanner) {

    }
}


