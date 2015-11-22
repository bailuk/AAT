package ch.bailu.aat.gpx.parser;

import java.io.IOException;

import org.osmdroid.util.GeoPoint;

import ch.bailu.aat.gpx.parser.XmlParser.ParserIO;

public class StateOsmNode extends StateOsmPoint {

    private final ParserState tag = new StateOsmTag();

    
    @Override
    public void parse(ParserIO io) throws IOException {
        parseNode(io);
        rememberNode(io);
        parseSubtags(io);
    }


    private void parseSubtags(ParserIO io) throws IOException {

        io.tagList.clear();
        
        while (true) {

            if (io.stream.haveA('/')) {// /> 
                io.stream.read();
                if (io.stream.haveA('>')) break;
            }

            if (io.stream.haveA('<')) {
                io.stream.read();
                if (io.stream.haveA('t')) { // <tag
                    tag.parse(io);

                } else if (io.stream.haveA('/')) { // </[node>]
                    break;
                }
            } 

            io.stream.read();
            if (io.stream.haveEOF()) break;
        }

        havePoint(io);
    }

    private void parseNode(ParserIO io) throws IOException {
        io.stream.to('=');
        io.stream.to('"');
        io.id.scan();

        io.stream.to('=');
        io.stream.to('"');
        io.latitude.scan();

        io.stream.to('=');
        io.stream.to('"');
        io.longitude.scan();
    }


    private void rememberNode(ParserIO io) throws IOException {
        io.nodeMap.put(io.id.getInt(), 
                new GeoPoint(io.latitude.getInt(), io.longitude.getInt()));
    }

    
    // <gpx oder <osm
    /*
    <osm version="0.6" generator="Overpass API">
    <note>The data included in this document is from www.openstreetmap.org. The data is made available under ODbL.</note>
    <meta osm_base="2012-11-29T14:56:02Z"/>
    <node id="26860669" lat="47.6437096" lon="8.8589682">
    <tag k="name" v="Hüttenberg"/>
    <tag k="tourism" v="camp_site"/>
    </node>
    </osm> 
     */		

}
