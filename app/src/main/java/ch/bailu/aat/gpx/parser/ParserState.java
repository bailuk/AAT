package ch.bailu.aat.gpx.parser;

import java.io.IOException;

import ch.bailu.aat.gpx.parser.XmlParser.ParserIO;

public abstract class ParserState {

    public abstract void parse(ParserIO io) throws IOException;

}
