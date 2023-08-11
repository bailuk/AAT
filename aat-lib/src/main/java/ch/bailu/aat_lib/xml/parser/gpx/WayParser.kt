package ch.bailu.aat_lib.xml.parser.gpx;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.coordinates.LatLongE6;
import ch.bailu.aat_lib.xml.parser.osm.Attr;
import ch.bailu.aat_lib.xml.parser.osm.MemberParser;
import ch.bailu.aat_lib.xml.parser.osm.NdParser;
import ch.bailu.aat_lib.xml.parser.osm.OsmTagParser;
import ch.bailu.aat_lib.xml.parser.osm.TagParser;
import ch.bailu.aat_lib.xml.parser.Util;
import ch.bailu.aat_lib.xml.parser.scanner.Scanner;
import ch.bailu.aat_lib.util.Objects;

public class WayParser extends TagParser {

    private final TagParser tag = new OsmTagParser();
    private final TagParser member = new MemberParser();
    private final TagParser nd = new NdParser();


    public WayParser() {
        this("way");
    }

    public WayParser(String tag) {
        super(tag);
    }


    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException {

        scanner.tags.clear();
        scanner.referencer.clear();


        new Attr(parser) {
            @Override
            public void attribute(String name, String value) throws IOException {
                if (Objects.equals(name, "id")) {
                    scanner.id.scan(value);
                    scanner.referencer.id = scanner.id.getInt();
                }
            }
        };
    }

    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return tag.parse(parser, scanner) ||
                nd.parse(parser, scanner) ||
                member.parse(parser, scanner);
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) throws IOException {
        if (scanner.referencer.resolved > 0) {
            rememberRelation(scanner);
            Util.wayPointParsed(scanner);
        }
    }

    private void rememberRelation(Scanner scanner) {
        BoundingBoxE6 b = scanner.referencer.bounding;

        LatLongE6 c = b.getCenter();

        scanner.latitude.setInt(c.getLatitudeE6());
        scanner.longitude.setInt(c.getLongitudeE6());
        scanner.referencer.put(scanner.referencer.id, c);
    }
}
