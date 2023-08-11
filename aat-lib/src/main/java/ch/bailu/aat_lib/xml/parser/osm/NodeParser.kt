package ch.bailu.aat_lib.xml.parser.osm;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat_lib.coordinates.LatLongE6;
import ch.bailu.aat_lib.xml.parser.Util;
import ch.bailu.aat_lib.xml.parser.scanner.Scanner;
import ch.bailu.aat_lib.util.Objects;

public class NodeParser extends TagParser {
    private final TagParser tag = new OsmTagParser();

    public NodeParser() {
        super("node");
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
                if (Objects.equals(name, "id")) {
                    scanner.id.scan(value);

                } else if (Objects.equals(name, "lat")) {
                    scanner.latitude.scan(value);


                } else if (Objects.equals(name, "lon")) {
                    scanner.longitude.scan(value);

                }
            }
        };
    }

    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return tag.parse(parser, scanner);
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) throws IOException {
        scanner.referencer.put(scanner.id.getInt(),
                new LatLongE6(scanner.latitude.getInt(), scanner.longitude.getInt()));

        Util.wayPointParsed(scanner);

    }
}
