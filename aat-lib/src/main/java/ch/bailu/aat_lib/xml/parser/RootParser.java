package ch.bailu.aat_lib.xml.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ch.bailu.aat_lib.xml.parser.gpx.GpxParser;
import ch.bailu.aat_lib.xml.parser.osm.OsmParser;
import ch.bailu.aat_lib.xml.parser.osm.SearchresultsParser;
import ch.bailu.aat_lib.xml.parser.osm.TagParser;
import ch.bailu.aat_lib.xml.parser.scanner.Scanner;

public class RootParser {
    final TagParser gpx = new GpxParser();
    final TagParser osm = new OsmParser();
    final TagParser nominatim = new SearchresultsParser();


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
