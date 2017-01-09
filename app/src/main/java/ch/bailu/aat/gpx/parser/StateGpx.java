package ch.bailu.aat.gpx.parser;

import java.io.IOException;

import ch.bailu.aat.gpx.parser.XmlParser.ParserIO;


public class StateGpx extends ParserState {


    @Override
    public void parse(ParserIO io) throws IOException {
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


    private void parseDateTime(ParserIO io) throws IOException {
        io.stream.skip(1);
        io.dateTime.parse();
    }


    private void parsePoint(ParserIO io, OnParsedInterface p) throws IOException {
        io.parsed.onHavePoint();
        io.parsed=p;

        io.stream.skip(3);

        DoubleParser parser=null;

        while (true)  {

            io.stream.read();
            if (io.stream.haveA('a')) {
                io.stream.skip(2);
                parser=io.latitude;
            } else if (io.stream.haveA('o')) {
                io.stream.skip(2);
                parser=io.longitude;
            } else if (io.stream.haveA('\"') && parser != null) {
                parser.scan();
            } else if (io.stream.haveA('>') || io.stream.haveEOF()) {
                break;
            }
        }
    }

    private void parseSegment(ParserIO io,OnParsedInterface p) throws IOException {
        io.parsed.onHavePoint();
        io.parsed=OnParsedInterface.NULL_ONPARSED;
        p.onHaveSegment();

        io.stream.skip(3);

    }


    private void parseAltitude(ParserIO io) throws IOException {
        io.stream.skip(2);
        io.altitude.scan();
    }

    private void parseName(ParserIO io) throws IOException {
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
