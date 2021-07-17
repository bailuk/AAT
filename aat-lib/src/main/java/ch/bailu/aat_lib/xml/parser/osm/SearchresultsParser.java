package ch.bailu.aat_lib.xml.parser.osm;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat_lib.xml.parser.scanner.Scanner;

public class SearchresultsParser extends TagParser {

    private final TagParser place = new PlaceParser();


    public SearchresultsParser() {
        super("searchresults");
    }

    @Override
    protected void parseText(XmlPullParser parser, Scanner scanner) throws IOException {

    }

    @Override
    protected void parseAttributes(XmlPullParser parser, Scanner scanner) throws IOException {

    }

    @Override
    protected boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return place.parse(parser, scanner);
    }

    @Override
    protected void parsed(XmlPullParser parser, Scanner scanner) {

    }
}
