package ch.bailu.aat.gpx.xml_parser.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat.gpx.xml_parser.parser.gpx.GpxParser;
import ch.bailu.aat.gpx.xml_parser.parser.nominatim.SearchresultsParser;
import ch.bailu.aat.gpx.xml_parser.parser.osm.OsmParser;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;

public class RootParser {
    TagParser gpx = new GpxParser();
    TagParser osm = new OsmParser();
    TagParser nominatim = new SearchresultsParser();


    public void parse(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {

        while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parseTags(parser, scanner) == false) {
                Util.skipTag(parser);
            }
            parser.next();
        }
    }

    public boolean parseTags(XmlPullParser parser, Scanner scanner) throws IOException, XmlPullParserException {
        return  gpx.parse(parser, scanner) ||
                osm.parse(parser, scanner) ||
                nominatim.parse(parser, scanner);
    }


}
