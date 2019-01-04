package ch.bailu.aat.gpx.xml_parser.parser.osm;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.gpx.xml_parser.parser.Attr;
import ch.bailu.aat.gpx.xml_parser.parser.TagParser;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;
import ch.bailu.util_java.util.Objects;

public class NodeParser extends AbsParser {
    private final TagParser tag = new OsmTagParser();

    public NodeParser() {
        super("node");
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
        scanner.nodeMap.put(scanner.id.getInt(),
                new LatLongE6(scanner.latitude.getInt(), scanner.longitude.getInt()));

        havePoint(scanner);

    }
}
