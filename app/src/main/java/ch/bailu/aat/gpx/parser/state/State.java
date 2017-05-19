package ch.bailu.aat.gpx.parser.state;

import java.io.IOException;

import ch.bailu.aat.gpx.parser.scanner.Scanner;


public abstract class State {

    public abstract void parse(Scanner io) throws IOException;

}
