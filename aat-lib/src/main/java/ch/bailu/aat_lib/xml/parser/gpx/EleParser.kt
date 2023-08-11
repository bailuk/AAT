package ch.bailu.aat_lib.xml.parser.gpx;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;

import ch.bailu.aat_lib.xml.parser.osm.TagParser;
import ch.bailu.aat_lib.xml.parser.scanner.Scanner;

public class EleParser extends TagParser {
    public EleParser() {
        super("ele");
    }

    @Override
    public void parseText(XmlPullParser parser, Scanner scanner) throws IOException {
        scanner.altitude.scan(parser.getText());
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
