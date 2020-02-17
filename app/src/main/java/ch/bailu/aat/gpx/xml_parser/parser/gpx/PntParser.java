package ch.bailu.aat.gpx.xml_parser.parser.gpx;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxConstants;
import ch.bailu.aat.gpx.xml_parser.parser.Attr;
import ch.bailu.aat.gpx.xml_parser.parser.TagParser;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;
import ch.bailu.util_java.util.Objects;

public abstract class PntParser extends TagParser {
    private final TagParser time = new TimeParser();
    private final TagParser ele = new EleParser();
    private final TagParser extensions = new ExtensionsParser();

    public PntParser(String t) {
        super(t);
    }

    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) {

    }

    @Override
    public void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException {
        scanner.tags.clear();

        new Attr(parser) {
            @Override
            public void attribute(String name, String value) throws IOException {
                if (Objects.equals(name, GpxConstants.QNAME_LATITUDE)) {
                    scanner.latitude.scan(value);

                } else if (Objects.equals(name, GpxConstants.QNAME_LONGITUDE)) {
                    scanner.longitude.scan(value);
                }

            }
        };
    }

    @Override
    public boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

        return ele.parse(parser, scanner) ||
                time.parse(parser, scanner) ||
                extensions.parse(parser, scanner);
    }

}
