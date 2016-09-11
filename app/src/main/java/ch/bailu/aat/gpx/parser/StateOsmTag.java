package ch.bailu.aat.gpx.parser;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxAttributes.Tag;
import ch.bailu.aat.gpx.parser.XmlParser.ParserIO;

public class StateOsmTag extends ParserState {
    private String key,value;

    
    @Override
    public void parse(ParserIO io) throws IOException {
        
        parseString(io, 'k');
        key=io.builder.toString();
        
        parseString(io, 'v');
        value=io.builder.toString();
        
        io.tagList.add(new Tag(key, value));
        
        io.stream.to('>');

    }

    private void parseString(ParserIO io, int c) throws IOException {
        io.stream.to(c);
        io.stream.to('=');
        io.stream.toQuotation();

        io.builder.setLength(0);
        
        while(true) {
            io.stream.read();

            if (io.stream.haveEOF() || io.stream.haveQuotation()) break;

            io.builder.append((char)io.stream.get());
        }
    }

}
