package ch.bailu.aat.gpx.parser;

import java.io.IOException;

import ch.bailu.aat.gpx.parser.XmlParser.ParserIO;

public class StateNominatim extends ParserState {

    private ParserState node = new StatePlaceNode(); 

    @Override
    public void parse(ParserIO io) throws IOException {
        //io.stream.to('p');  // timestamp='
        //io.stream.to('=');
        //io.stream.toQuotation();
        //io.dateTime.parse();
        
        
        while (true) {
            io.stream.to('<');
            io.stream.read();

            if (io.stream.haveEOF()) {
                break;

            } else if (io.stream.haveA('p')) {
                node.parse(io);
            }
        }        
    }

}
