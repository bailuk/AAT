package ch.bailu.aat_lib.xml.parser.gpx;

import org.xmlpull.v1.XmlPullParser;

import ch.bailu.aat_lib.xml.parser.osm.TagParser;
import ch.bailu.aat_lib.xml.parser.scanner.Scanner;

public class GpxExtensionParser extends TagParser {


    private String key = null;


    public GpxExtensionParser() {
        super(null);
    }


    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) {
        final String val = parser.getText();
        if (val != null && key != null && key.length() > 0) {
            scanner.tags.add(key, val);

        }
    }


    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) {
        key = parser.getName();

    }

    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) {
        return false;
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) {

    }
}
