package ch.bailu.aat.gpx.xml_parser.parser.gpx;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxConstants;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;

public class RteptParser extends PntParser{
    public RteptParser() {
        super(GpxConstants.QNAME_ROUTE_POINT);
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) throws IOException {
        scanner.routeParsed.onHavePoint();
    }
}
