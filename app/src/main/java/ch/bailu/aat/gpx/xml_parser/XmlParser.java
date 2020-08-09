package ch.bailu.aat.gpx.xml_parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Reader;

import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.xml_parser.parser.RootParser;
import ch.bailu.aat.gpx.xml_parser.scanner.Scanner;
import ch.bailu.aat.gpx.xml_parser.parser.AbsXmlParser;
import ch.bailu.foc.Foc;
import ch.bailu.util_java.parser.OnParsedInterface;

public class XmlParser extends AbsXmlParser {


    private final Scanner scanner;
    private final Reader reader;
    private final XmlPullParser parser;

    public XmlParser(Foc file) throws IOException, XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

        factory.setNamespaceAware(true);

        parser = factory.newPullParser();
        reader = BOM.open(file);
        scanner = new Scanner(file.lastModified());
    }


    @Override
    public void parse() throws XmlPullParserException, IOException {
        parser.setInput(reader);
        new RootParser().parse(parser, scanner);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }



    @Override
    public void setOnRouteParsed(OnParsedInterface route) {
        scanner.routeParsed = route;
    }


    @Override
    public void setOnTrackParsed(OnParsedInterface track) {
        scanner.trackParsed = track;
    }


    @Override
    public void setOnWayParsed(OnParsedInterface way) {
        scanner.wayParsed = way;
    }


    @Override
    public int getLatitudeE6() {return scanner.latitude.getInt();}

    @Override
    public int getLongitudeE6() {return scanner.longitude.getInt();}

    @Override
    public double getAltitude() {
        return (double) scanner.altitude.getInt();
    }

    @Override
    public long getTimeStamp() {return scanner.dateTime.getTimeMillis();}


    @Override
    public GpxAttributes getAttributes() {
        return scanner.tags.get();
    }

    @Override
    public double getLongitude() {
        return getLongitudeE6() / 1E6;
    }

    @Override
    public double getLatitude() {
        return getLatitudeE6() / 1E6;
    }

}

