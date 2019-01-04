package ch.bailu.aat.gpx.xml_parser.parser.osm;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.gpx.xml_parser.parser.Attr;
import ch.bailu.aat.gpx.xml_parser.parser.TagParser;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;
import ch.bailu.util_java.util.Objects;

public class MemberParser extends TagParser {


    private final WayParser.Dereferencer dereferencer;


    public MemberParser(WayParser.Dereferencer d) {
        super("member");
        dereferencer = d;
    }

    public MemberParser(String tag, WayParser.Dereferencer d) {
        super(tag);
        dereferencer = d;
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
                if (Objects.equals(name, "ref")) {
                    scanner.id.scan(value);

                    LatLongE6 point = scanner.nodeMap.get(scanner.id.getInt());

                    if (point != null) {
                        dereferencer.bounding.add(point);
                        dereferencer.dereferenced++;
                    }
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
    }
}
