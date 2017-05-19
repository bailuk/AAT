package ch.bailu.aat.gpx.parser.state;

import java.io.IOException;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.gpx.parser.scanner.Scanner;

public class StateOsmRelation extends StateOsmPoint {
    private final State tag = new StateOsmTag();
    
    private BoundingBoxE6 bounding = BoundingBoxE6.NULL_BOX;
    private int myId=0;
    private int dereferenced;
    
    @Override
    public void parse(Scanner io) throws IOException {
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

    
    private void parseId(Scanner io) throws IOException {
        io.stream.to('"');
        io.id.scan();
        myId=io.id.getInt();
    }

    private void rememberRelation(Scanner io) throws IOException {
        BoundingBoxE6 b = bounding;

        LatLongE6 c = b.getCenter();

        io.latitude.setInt(c.getLatitudeE6());
        io.longitude.setInt(c.getLongitudeE6());

        io.nodeMap.put(myId, c);
    }
    
    private void parseRef(Scanner io) throws IOException {
        io.stream.to('r');
        io.stream.to('e');
        io.stream.to('f');
        io.stream.to('=');
        io.stream.to('"');
        io.id.scan();
        LatLongE6 point = io.nodeMap.get(io.id.getInt());

        if (point != null) {
            bounding.add(point);
            dereferenced++;
        }
    }
}
