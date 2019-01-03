package ch.bailu.aat.gpx.parser;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxAttributesStatic;
import ch.bailu.aat.gpx.GpxAttributesStatic.Tag;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.gpx.parser.scanner.Scanner;
import ch.bailu.aat.gpx.parser.state.State;
import ch.bailu.aat.gpx.parser.state.StateXml;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.parser.OnParsedInterface;

public class XmlParser extends AbsXmlParser {


    private final Scanner scanner;
    private final State state = new StateXml();



    public XmlParser(Foc file) throws IOException, SecurityException{
        scanner = new Scanner(file);
    }

    public void parse() throws IOException, SecurityException {
        state.parse(scanner);
    }


    public int getLatitudeE6() {return scanner.latitude.getInt();}
    public int getLongitudeE6() {return scanner.longitude.getInt();}
    public short getAltitude() {
        return (short) scanner.altitude.getInt();
    }
    
    public long getTimeStamp() {return scanner.dateTime.getTimeMillis();}

    
    public void setOnWayParsed(OnParsedInterface p) {
        scanner.wayParsed=p;
    }
    public void setOnRouteParsed(OnParsedInterface p) {
        scanner.routeParsed=p;
    }
    public void setOnTrackParsed(OnParsedInterface p) {
        scanner.trackParsed=p;
    }

    
    public GpxAttributes getAttributes() {
        return new GpxAttributesStatic(scanner.tagList.toArray(new Tag[]{}));
    }

    
    @Override
    public void close() throws IOException {
        scanner.stream.close();
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
