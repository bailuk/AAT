package ch.bailu.aat.gpx.parser;

import java.io.IOException;
import java.util.Collections;

import ch.bailu.aat.gpx.parser.XmlParser.ParserIO;

public class StatePlaceNode extends ParserState {

    private final ParserState tag = new StateSimpleTag();
    
    @Override
    public void parse(ParserIO io) throws IOException {
        io.stream.to(' ');
        io.tagList.clear();
        parseSubtags(io);
        
        if (io.tagList.size()>0) {
            Collections.sort(io.tagList);
            io.wayParsed.onHavePoint();
        }

        //io.parsed.onHavePoint();
    }
    
    


    private void parseSubtags(ParserIO io) throws IOException {

        
        
        
        while (true) {
            if (io.stream.haveA('>')) { 
                io.stream.read();
                break;
                
            } else if (io.stream.haveCharacter()) {
                tag.parse(io);
            } 

            io.stream.read();
            if (io.stream.haveEOF()) break;
        }
    }

}
