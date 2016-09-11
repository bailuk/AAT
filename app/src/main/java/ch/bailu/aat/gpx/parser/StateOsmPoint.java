package ch.bailu.aat.gpx.parser;

import java.io.IOException;
import java.util.Collections;

import ch.bailu.aat.gpx.parser.XmlParser.ParserIO;

public abstract class StateOsmPoint extends ParserState {

    public void havePoint(ParserIO io) throws IOException {
        
        
        
        
        if (io.tagList.size()>0) {
            Collections.sort(io.tagList);
            io.wayParsed.onHavePoint();
        }
    }
}
