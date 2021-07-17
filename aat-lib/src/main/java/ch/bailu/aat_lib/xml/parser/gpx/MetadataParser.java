package ch.bailu.aat_lib.xml.parser.gpx;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat_lib.xml.parser.osm.TagParser;
import ch.bailu.aat_lib.xml.parser.gpx.TimeParser;
import ch.bailu.aat_lib.xml.parser.scanner.Scanner;

public class MetadataParser  extends TagParser {
    private final TimeParser time = new TimeParser();

    public MetadataParser() {
        super("metadata");
    }


    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) {

    }

    @Override
    public boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return time.parse(parser, scanner);
    }

    @Override
    public void parsed(XmlPullParser parser, Scanner scanner) {

    }
}
