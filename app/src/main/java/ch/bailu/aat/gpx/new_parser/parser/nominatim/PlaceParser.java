package ch.bailu.aat.gpx.new_parser.parser.nominatim;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Collections;

import ch.bailu.aat.gpx.GpxAttributesStatic;
import ch.bailu.aat.gpx.new_parser.parser.Attr;
import ch.bailu.aat.gpx.new_parser.parser.TagParser;
import ch.bailu.aat.gpx.new_parser.scanner.Scanner;
import ch.bailu.util_java.util.Objects;

public class PlaceParser extends TagParser {
    public PlaceParser() {
        super("place");
    }

    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        scanner.tagList.clear();

        new Attr(parser) {
            @Override
            public void attribute(String name, String value) throws IOException {
                if (Objects.equals(name, "lat")) {
                    scanner.latitude.scan(value);

                } else if (Objects.equals(name, "lon")) {
                    scanner.longitude.scan(value);

                } else {
                    scanner.tagList.add(new GpxAttributesStatic.Tag(name, value));

                }
            }
        };
    }

    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return false;
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) throws IOException {
        if (scanner.tagList.size()>0) {
            Collections.sort(scanner.tagList);
            scanner.wayParsed.onHavePoint();
        }
    }
}
