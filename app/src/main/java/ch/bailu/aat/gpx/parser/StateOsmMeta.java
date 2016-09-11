package ch.bailu.aat.gpx.parser;

import java.io.IOException;

import ch.bailu.aat.gpx.parser.XmlParser.ParserIO;

public class StateOsmMeta extends ParserState {

    @Override
    public void parse(ParserIO io) throws IOException {
        io.stream.to('"');
        io.dateTime.parse();
    }

}
