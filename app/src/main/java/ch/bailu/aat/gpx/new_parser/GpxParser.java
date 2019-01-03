package ch.bailu.aat.gpx.new_parser;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class GpxParser {

    private String seg = "trkseg";
    private String pnt = "trkpnt";
    private String trk = "trk";

    public GpxParser(XmlPullParser parser) throws XmlPullParserException, IOException {


        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equals("time")) {
                    // parseTime()

                } else if (parser.getName().equals("trk")) {
                    seg = "trkseg";
                    pnt = "trkpnt";
                    trk = "trk";

                    parseTrack(parser);
                } else if (parser.getName().equals("way")) {
                    seg = "wayseg";
                    pnt = "waypnt";
                    trk = "way";
                    parseTrack(parser);
                } else if (parser.getName().equals("rte")) {
                    seg = "rteseg";
                    pnt = "rtepnt";
                    trk = "rte";
                    parseTrack(parser);
                }

            } else if (parser.getEventType() == XmlPullParser.END_TAG) {
                if (parser.getName().equals("gpx")) {
                    return;
                }
            }
            parser.next();
        }
    }

    private void parseTrack(XmlPullParser parser) throws XmlPullParserException, IOException {

        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equals("time")) {
                    // parseTime()
                } if (parser.getName().equals(seg)) {
                    // newSegment();

                } else if (parser.getName().equals(pnt)) {

                } else if (parser.getName().equals("ele")) {

                }

            } else if (parser.getEventType() == XmlPullParser.END_TAG) {
                if (parser.getName().equals(trk)) {
                    return;
                }
            }
            parser.next();
        }
    }


    }
}


