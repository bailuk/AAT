package ch.bailu.aat.gpx.parser.state;

import java.io.IOException;
import java.util.Collections;

import ch.bailu.aat.gpx.parser.scanner.Scanner;


public class StatePlaceNode extends State {

    private final State tag = new StateSimpleTag();
    
    @Override
    public void parse(Scanner io) throws IOException {
        io.stream.to(' ');
        io.tagList.clear();
        parseSubtags(io);
        
        if (io.tagList.size()>0) {
            Collections.sort(io.tagList);
            io.wayParsed.onHavePoint();
        }

        //io.parsed.onHavePoint();
    }
    
    


    private void parseSubtags(Scanner io) throws IOException {

        
        
        
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
