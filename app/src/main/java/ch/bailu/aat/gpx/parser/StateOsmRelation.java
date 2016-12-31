package ch.bailu.aat.gpx.parser;

import org.osmdroid.util.BoundingBoxOsm;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.parser.XmlParser.ParserIO;

public class StateOsmRelation extends StateOsmPoint {
    private final ParserState tag = new StateOsmTag();
    
    private BoundingBoxE6 bounding = BoundingBoxE6.NULL_BOX;
    private int myId=0;
    private int dereferenced;
    
    @Override
    public void parse(ParserIO io) throws IOException {
        bounding = new BoundingBoxE6();

        io.tagList.clear();
        dereferenced=0;
        
        parseId(io);
        
        
        while (true) {
            io.stream.to('<');
            io.stream.read();

            if (io.stream.haveA('m') || io.stream.haveA('n')) { 
                // <member ... ref="" .../> or <nd ref=""/> 
                parseRef(io);

            } else if (io.stream.haveA('t')) {
                // <tag k="" v=""/> pair follows
                tag.parse(io);

            } else if (io.stream.haveA('/')) {
                break;

            } else if (io.stream.haveEOF()) {
                break;
            }
        }

        if (dereferenced>0) {
            rememberRelation(io);
            havePoint(io);
        }
    }

    
    private void parseId(ParserIO io) throws IOException {
        io.stream.to('"');
        io.id.scan();
        myId=io.id.getInt();
    }

    private void rememberRelation(ParserIO io) throws IOException {
        BoundingBoxOsm b = bounding.toBoundingBoxE6();

        GeoPoint c = b.getCenter();

        io.latitude.setInt(c.getLatitudeE6());
        io.longitude.setInt(c.getLongitudeE6());

        io.nodeMap.put(myId, c);
    }
    
    private void parseRef(ParserIO io) throws IOException {
        io.stream.to('r');
        io.stream.to('e');
        io.stream.to('f');
        io.stream.to('=');
        io.stream.to('"');
        io.id.scan();
        GeoPoint point = io.nodeMap.get(io.id.getInt());

        if (point != null) {
            bounding.add(point);
            dereferenced++;
        }
    }
}
