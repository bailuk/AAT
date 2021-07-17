package ch.bailu.aat_lib.xml.parser.osm;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;

import ch.bailu.aat_lib.xml.parser.scanner.Scanner;
import ch.bailu.util.Objects;

public class OsmTagParser extends TagParser {
    public OsmTagParser() {
        super(OsmConstants.T_TAG);
    }

    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException {
        final String[] k = {""};
        final String[] v = {""};

        new Attr(parser) {
            @Override
            public void attribute(String name, String value) {
                if (Objects.equals(name, OsmConstants.A_KEY)) {
                    k[0] =value;
                } else if (Objects.equals(name, OsmConstants.A_VALUE)) {
                    v[0] =value;
                }
            }
        };
        scanner.tags.add(k[0], v[0]);
    }


    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) {
        return false;
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) {

    }
}
