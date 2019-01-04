package ch.bailu.aat.gpx.new_parser.parser.osm;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.new_parser.parser.TagParser;
import ch.bailu.aat.gpx.new_parser.scanner.Scanner;

public class OsmParser extends TagParser {

    private final TagParser meta = new MetaParser();
    private final TagParser node = new NodeParser();
    private final TagParser relation = new RelationParser();
    private final TagParser way = new WayParser();

    public OsmParser() {
        super("osm");
    }

    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

    }

    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return node.parse(parser, scanner) ||
                relation.parse(parser, scanner) ||
                way.parse(parser, scanner) ||
                meta.parse(parser, scanner);
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) throws IOException {

    }
}
