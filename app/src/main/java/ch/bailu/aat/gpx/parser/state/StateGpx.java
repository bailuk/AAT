package ch.bailu.aat.gpx.parser.state;

import java.io.IOException;

import ch.bailu.aat.gpx.parser.scanner.Scanner;
import ch.bailu.util_java.parser.OnParsedInterface;
import ch.bailu.util_java.parser.scanner.DoubleScanner;


public class StateGpx extends State {


    @Override
    public void parse(Scanner io) throws IOException {
        while (true) {
            io.stream.read();

            if (io.stream.haveA('<')) {
                io.stream.read();

                if (io.stream.haveA('t')) { // trk, trkseg, trkpt or time
                    io.stream.skip(2);
                    io.stream.read();

                    if (io.stream.haveA('p')) {
                        parsePoint(io, io.trackParsed);


                    } else if (io.stream.haveA('s')) {
                        parseSegment(io, io.trackParsed);

                    } else if (io.stream.haveA('e')) {
                        parseDateTime(io);
                    }

                } else if (io.stream.haveA('e')) {
                    io.stream.read();
                    if (io.stream.haveA('l'))
                        parseAltitude(io);

                } else if (io.stream.haveA('n')) {
                    io.stream.read();
                    parseName(io);

                } else if (io.stream.haveA('w')) {
                    parsePoint(io, io.wayParsed); // wpt

                } else if (io.stream.haveA('r')) {
                    io.stream.skip(2);
                    io.stream.read();

                    if (io.stream.haveA('p')) {
                        parsePoint(io, io.routeParsed);
                    }


                } else {
                    io.stream.skip(3);
                }


            } if (io.stream.haveEOF()) {
                break;
            }
        }

        io.parsed.onHavePoint();
    }


    private void parseDateTime(Scanner io) throws IOException {
        io.stream.skip(1);
        io.dateTime.scan(io.stream);
    }


    private void parsePoint(Scanner io, OnParsedInterface p) throws IOException {
        io.parsed.onHavePoint();
        io.parsed=p;

        io.stream.skip(3);

        DoubleScanner parser=null;

        while (true)  {

            io.stream.read();
            if (io.stream.haveA('a')) {
                io.stream.skip(2);
                parser=io.latitude;
            } else if (io.stream.haveA('o')) {
                io.stream.skip(2);
                parser=io.longitude;
            } else if (io.stream.haveA('\"') && parser != null) {
                parser.scan(io.stream);
            } else if (io.stream.haveA('>') || io.stream.haveEOF()) {
                break;
            }
        }
    }

    private void parseSegment(Scanner io, OnParsedInterface p) throws IOException {
        io.parsed.onHavePoint();
        io.parsed=OnParsedInterface.NULL;
        p.onHaveSegment();

        io.stream.skip(3);

    }


    private void parseAltitude(Scanner io) throws IOException {
        io.stream.skip(2);
        io.altitude.scan(io.stream);
    }

    private void parseName(Scanner io) throws IOException {
        io.stream.skip(4);

        io.builder.setLength(0);
        while(true) {
            io.stream.read();
            if (io.stream.haveA('>') || io.stream.haveEOF()) {
                break;
            } else {
                io.builder.append((char)io.stream.get());
            }
        }
    }


}
