package ch.bailu.aat.gpx.parser.state;

import java.io.IOException;

import ch.bailu.aat.gpx.parser.scanner.Scanner;


public class StateOsm extends State {

    private final State node = new StateOsmNode();
    private final State meta = new StateOsmMeta();
    private final State relation = new StateOsmRelation();


    @Override
    public void parse(Scanner io) throws IOException {

        while (true) {
            io.stream.to('<');
            io.stream.read();

            if (io.stream.haveEOF()) {
                break;

            } else if (io.stream.haveA('n')) {
                io.stream.read();
                io.stream.read();
                if (io.stream.haveA('d')) { // <node
                    node.parse(io);
                }


            } else if (io.stream.haveA('w') || io.stream.haveA('r')) {
                // <relation id="">  or <way id="">
                relation.parse(io);

            } else if (io.stream.haveA('m')) { // <meta
                meta.parse(io);
                
            }
        }
    }


    // <gpx oder <osm
    /*
    <osm version="0.6" generator="Overpass API">
    <note>The data included in this document is from www.openstreetmap.org. The data is made available under ODbL.</note>
    <meta osm_base="2012-11-29T14:56:02Z"/>
    <node id="26860669" lat="47.6437096" lon="8.8589682">
    <tag k="getName" v="Hüttenberg"/>
    <tag k="tourism" v="camp_site"/>
    </node>
    </osm> 
     */

}
