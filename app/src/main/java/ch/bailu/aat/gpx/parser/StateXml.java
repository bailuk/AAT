package ch.bailu.aat.gpx.parser;

import java.io.IOException;

import ch.bailu.aat.gpx.parser.XmlParser.ParserIO;

public class StateXml extends ParserState {

    private final ParserState stateGpx = new StateGpx();
    private final ParserState stateOsm = new StateOsm();
    private final ParserState stateNominatim = new StateNominatim();

    @Override
    public void parse(ParserIO io) throws IOException {


        while(true) {
            io.stream.to('<');
            io.stream.read();

            if (io.stream.haveEOF()) {
                //AppLog.d(this, "EOF");
                break;

            } else 	if (io.stream.haveA('g')) { // <gpx
                //AppLog.d(this, "gpx");
                stateGpx.parse(io);
                break;

            } else if (io.stream.haveA('o')) { // <osm
                //AppLog.d(this, "osm");
                stateOsm.parse(io);
                break;

            } else if (io.stream.haveA('s')) { //<searchresults
                stateNominatim.parse(io);
                break;
            }
        }
    }

}
