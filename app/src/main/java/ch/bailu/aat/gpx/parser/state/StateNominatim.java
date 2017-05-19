package ch.bailu.aat.gpx.parser.state;

import java.io.IOException;

import ch.bailu.aat.gpx.parser.scanner.Scanner;


public class StateNominatim extends State {

    private final State node = new StatePlaceNode();

    @Override
    public void parse(Scanner io) throws IOException {
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
