package ch.bailu.aat.gpx.new_parser.parser.osm;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.gpx.new_parser.parser.Attr;
import ch.bailu.aat.gpx.new_parser.parser.TagParser;
import ch.bailu.aat.gpx.new_parser.scanner.Scanner;
import ch.bailu.util_java.util.Objects;

public class WayParser extends AbsParser {
    public static class Dereferencer {
        public int id=0;
        public int dereferenced = 0;
        public BoundingBoxE6 bounding = BoundingBoxE6.NULL_BOX;
    }

    private final TagParser tag = new OsmTagParser();
    private final TagParser member;
    private final TagParser nd;

    private final Dereferencer dereferencer = new Dereferencer();

    public WayParser() {
        this("way");
    }

    public WayParser(String tag) {
        super(tag);
        member = new MemberParser(dereferencer);
        nd = new NdParser(dereferencer);
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
                    dereferencer.id = scanner.id.getInt();
                }
            }
        };
    }

    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return tag.parse(parser, scanner) ||
                member.parse(parser, scanner) ||
                nd.parse(parser, scanner);
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) throws IOException {
        if (dereferencer.dereferenced > 0) {
            havePoint(scanner);
            rememberRelation(scanner);
        }
    }

    private void rememberRelation(Scanner scanner) throws IOException {
        BoundingBoxE6 b = dereferencer.bounding;

        LatLongE6 c = b.getCenter();

        scanner.latitude.setInt(c.getLatitudeE6());
        scanner.longitude.setInt(c.getLongitudeE6());
        scanner.nodeMap.put(dereferencer.id, c);
    }
}
