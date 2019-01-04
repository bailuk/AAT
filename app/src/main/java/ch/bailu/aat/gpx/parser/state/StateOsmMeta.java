package ch.bailu.aat.gpx.parser.state;

import java.io.IOException;

import ch.bailu.aat.gpx.parser.scanner.Scanner;


public class StateOsmMeta extends State {

    @Override
    public void parse(Scanner io) throws IOException {
        io.stream.to('"');
        io.dateTime.scan(io.stream);
    }

}
