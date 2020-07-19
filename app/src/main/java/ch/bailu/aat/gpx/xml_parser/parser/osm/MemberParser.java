package ch.bailu.aat.gpx.xml_parser.parser.osm;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;

import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.gpx.xml_parser.parser.Attr;
import ch.bailu.aat.gpx.xml_parser.parser.TagParser;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;
import ch.bailu.util_java.util.Objects;

public class MemberParser extends TagParser {

    public MemberParser() {
        this("member");
    }
    public MemberParser(String tag) {
        super(tag);
    }


    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) throws IOException {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException {
        new Attr(parser) {
            @Override
            public void attribute(String name, String value) throws IOException {
                if (Objects.equals(name, "ref")) {
                    scanner.id.scan(value);

                    LatLongE6 point = scanner.referencer.get(scanner.id.getInt());

                    if (point != null) {
                        scanner.referencer.bounding.add(point);
                        scanner.referencer.resolved++;
                    }
                }
            }
        };
    }


    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) {
        return false;
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) {
    }
}
