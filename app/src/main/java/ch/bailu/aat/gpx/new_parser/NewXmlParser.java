package ch.bailu.aat.gpx.new_parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.parser.AbsXmlParser;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.parser.OnParsedInterface;

public class NewXmlParser extends AbsXmlParser {

    private final InputStream stream;
    private final XmlPullParser parser;

    public NewXmlParser(Foc file) throws IOException, XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

        factory.setNamespaceAware(true);

        parser = factory.newPullParser();
        stream = file.openR();
    }

    @Override
    public void setOnRouteParsed(OnParsedInterface route) {

    }

    @Override
    public void setOnTrackParsed(OnParsedInterface track) {

    }

    @Override
    public void setOnWayParsed(OnParsedInterface way) {

    }

    @Override
    public void parse() throws XmlPullParserException, IOException {
        parser.setInput(new InputStreamReader(stream));


        int eventType = parser.getEventType();


        while(eventType != XmlPullParser.END_DOCUMENT) {

            if(eventType == XmlPullParser.START_DOCUMENT) {


                System.out.println("Start document");
            } else if(eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("gpx")) {

                } else if (parser.getName().equals("osm")) {

                }


                System.out.println("Start tag "+parser.getName());


            } else if(eventType == XmlPullParser.END_TAG) {
                System.out.println("End tag "+parser.getName());
            } else if(eventType == XmlPullParser.TEXT) {
                System.out.println("Text "+parser.getText());
            }


            eventType = parser.next();
        }
        System.out.println("End document");

    }

    @Override
    public short getAltitude() {
        return 0;
    }

    @Override
    public double getLongitude() {
        return 0;
    }

    @Override
    public double getLatitude() {
        return 0;
    }

    @Override
    public long getTimeStamp() {
        return 0;
    }

    @Override
    public GpxAttributes getAttributes() {
        return null;
    }

    @Override
    public int getLatitudeE6() {
        return 0;
    }

    @Override
    public int getLongitudeE6() {
        return 0;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}

