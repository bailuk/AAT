package ch.bailu.aat.gpx.parser.state;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxAttributesStatic.Tag;
import ch.bailu.aat.gpx.parser.scanner.Scanner;

public class StateSimpleTag extends State {
    
    
    
    @Override
    public void parse(Scanner io) throws IOException {
        String key="";
        
        while (true) {
            if (io.stream.haveQuotation()) {
                if (key.equals("lat")) {
                    io.latitude.scan(io.stream);
                } else if (key.equals("lon")) {
                    io.longitude.scan(io.stream);
                } else {
                    parseQuotedString(io);
                    final String value = io.builder.toString();
                    io.tagList.add(new Tag(key, value));
                }
                break;
                
            } else if (io.stream.haveCharacter()) {
                parseSimpleString(io);
                key = io.builder.toString();
            }
            

            io.stream.read();
            if (io.stream.haveEOF() || io.stream.haveA('>')) break;
        }
    }




    private void parseSimpleString(Scanner io) throws IOException {
        io.builder.setLength(0);
        while(true) {
            if (io.stream.haveCharacter() || io.stream.haveDigit()) {
                io.builder.append((char)io.stream.get());
            } else {
                break;
            }
            io.stream.read();
        }
    }

    
    private void parseQuotedString(Scanner io) throws IOException {
        io.builder.setLength(0);
        
        while(true) {
            io.stream.read();
            
            if (io.stream.haveEOF() || io.stream.haveQuotation()) { 
                break;
            } else {
                io.builder.append((char)io.stream.get());
            }
        }
    }

}
