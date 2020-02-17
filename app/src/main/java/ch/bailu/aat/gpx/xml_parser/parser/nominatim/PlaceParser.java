package ch.bailu.aat.gpx.xml_parser.parser.nominatim;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.xml_parser.parser.Attr;
import ch.bailu.aat.gpx.xml_parser.parser.TagParser;
import ch.bailu.aat.gpx.xml_parser.parser.Util;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;
import ch.bailu.util_java.util.Objects;

public class PlaceParser extends TagParser {
    public PlaceParser() {
        super("place");
    }

    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException {
        scanner.tags.clear();

        new Attr(parser) {
            @Override
            public void attribute(String name, String value) throws IOException {
                if (Objects.equals(name, "lat")) {
                    scanner.latitude.scan(value);

                } else if (Objects.equals(name, "lon")) {
                    scanner.longitude.scan(value);

                } else {
                    scanner.tags.add(name, value);

                }
            }
        };
    }

    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) {
        return false;
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) throws IOException {
        Util.wayPointParsed(scanner);
    }
}
