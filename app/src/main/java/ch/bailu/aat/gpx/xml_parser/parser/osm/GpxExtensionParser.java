package ch.bailu.aat.gpx.xml_parser.parser.osm;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.xml_parser.parser.TagParser;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;
import ch.bailu.aat.util.ui.AppLog;

public class GpxExtensionParser extends TagParser {


    private String key = null;


    public GpxExtensionParser() {
        super(null);
    }


    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        final String val = parser.getText();
        if (val != null && key != null && key.length() > 0) {
            scanner.tags.add(key, val);

        }
    }


    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        key = parser.getName();

    }

    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return false;
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) throws IOException {

    }
}
