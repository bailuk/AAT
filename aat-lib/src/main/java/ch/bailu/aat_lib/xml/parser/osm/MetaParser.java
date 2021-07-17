package ch.bailu.aat_lib.xml.parser.osm;


import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;

import ch.bailu.aat_lib.xml.parser.scanner.Scanner;


import ch.bailu.util.Objects;

public class MetaParser extends TagParser {
    public MetaParser() {
        super("meta");
    }

    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) throws IOException {

    }


    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException {
        new Attr(parser) {
            @Override
            public void attribute(String name, String value) throws IOException {
                if (Objects.equals(name, "osm_base")) {
                    scanner.dateTime.scan(value);
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
