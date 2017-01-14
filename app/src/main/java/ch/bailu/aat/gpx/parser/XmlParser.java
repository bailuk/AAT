package ch.bailu.aat.gpx.parser;

import android.util.SparseArray;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxAttributesStatic;
import ch.bailu.aat.gpx.GpxAttributesStatic.Tag;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.util.fs.AbsAccess;

public class XmlParser implements Closeable, GpxPointInterface {


    private ParserIO io;
    private final ParserState state = new StateXml();

    public class ParserIO {
        public final SimpleStream stream;
        public final DoubleParser latitude,longitude,altitude,id; 
        public final DateScanner dateTime;
        public final StringBuilder builder=new StringBuilder();
        


        public OnParsedInterface 
        wayParsed	  = OnParsedInterface.NULL_ONPARSED,
        routeParsed	  = OnParsedInterface.NULL_ONPARSED, 
        trackParsed	  = OnParsedInterface.NULL_ONPARSED, 
        parsed        = OnParsedInterface.NULL_ONPARSED;

        public final SparseArray<LatLongE6> nodeMap = new SparseArray<>(50);
        public final ArrayList<GpxAttributesStatic.Tag> tagList = new ArrayList<>();
        
        private ParserIO(AbsAccess file) throws IOException {
            stream = new SimpleStream(file);

            latitude = new DoubleParser(stream,6);
            longitude = new DoubleParser(stream,6);
            altitude = new DoubleParser(stream,0);
            id = new DoubleParser(stream,0);
            dateTime = new DateScanner(stream, file.lastModified()); 
        }
    }

    public XmlParser(AbsAccess file) throws IOException {
        io = new ParserIO(file);
    }

    public void parse() throws IOException {
        state.parse(io);
    }


    public int getLatitudeE6() {return io.latitude.getInt();}
    public int getLongitudeE6() {return io.longitude.getInt();}
    public short getAltitude() {
        return (short) io.altitude.getInt();
    }
    
    public long getTimeStamp() {return io.dateTime.getTimeMillis();}

    
    public void setOnWayParsed(OnParsedInterface p) {
        io.wayParsed=p;
    }
    public void setOnRouteParsed(OnParsedInterface p) {
        io.routeParsed=p;
    }
    public void setOnTrackParsed(OnParsedInterface p) {
        io.trackParsed=p;
    }

    
    public GpxAttributes getAttributes() {
        return new GpxAttributesStatic(io.tagList.toArray(new Tag[]{}));
    }

    
    @Override
    public void close() throws IOException {
        io.stream.close();
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
