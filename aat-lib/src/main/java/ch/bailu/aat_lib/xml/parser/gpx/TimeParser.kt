package ch.bailu.aat_lib.xml.parser.gpx;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;

import ch.bailu.aat_lib.gpx.GpxConstants;
import ch.bailu.aat_lib.xml.parser.osm.TagParser;
import ch.bailu.aat_lib.xml.parser.scanner.Scanner;

public class TimeParser extends TagParser {

    public TimeParser() {
        super(GpxConstants.QNAME_TIME);
    }

    @Override
    public void parseText(XmlPullParser parser, Scanner scanner) throws IOException {
        scanner.dateTime.scan(parser.getText());
    }


    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) {

    }

    @Override
    public boolean parseTags(XmlPullParser parser, Scanner scanner) {
        return false;
    }

    @Override
    public void parsed(XmlPullParser parser, Scanner scanner) {

    }
}
