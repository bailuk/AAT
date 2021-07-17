package ch.bailu.aat_lib.xml.parser.gpx;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;

import ch.bailu.aat_lib.gpx.GpxConstants;
import ch.bailu.aat_lib.xml.parser.scanner.Scanner;

public class WptParser extends PntParser {


    public WptParser() {
        super(GpxConstants.QNAME_WAY_POINT);
    }


    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) throws IOException {
        scanner.wayParsed.onHavePoint();
    }
}
