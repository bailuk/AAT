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
    private final TimeParser time = new TimeParser();
    private final EleParser ele = new EleParser();

    public PntParser(String t) {
        super(t);
    }

    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

    }

    @Override
    public void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
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

        return ele.parse(parser, scanner) || time.parse(parser, scanner);
    }

}
