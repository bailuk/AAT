package ch.bailu.aat.gpx.parser.state;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxAttributesStatic.Tag;
import ch.bailu.aat.gpx.parser.scanner.Scanner;

public class StateOsmTag extends State {

    @Override
    public void parse(Scanner io) throws IOException {
        
        parseString(io, 'k');
        final String key=io.builder.toString();
        
        parseString(io, 'v');
        final String value=io.builder.toString();
        
        io.tagList.add(new Tag(key, value));
        
        io.stream.to('>');

    }

    private void parseString(Scanner io, int c) throws IOException {
        io.stream.to(c);
        io.stream.to('=');
        io.stream.toQuotation();

        io.builder.setLength(0);
        
        while(true) {
            io.stream.read();

            if (io.stream.haveEOF() || io.stream.haveDoubleQuote()) break;

            io.builder.append((char)io.stream.get());
        }
    }

}
