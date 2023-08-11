package ch.bailu.aat_lib.xml.parser.scanner;


import ch.bailu.aat_lib.xml.parser.util.DateScanner;
import ch.bailu.aat_lib.xml.parser.util.DoubleScanner;
import ch.bailu.aat_lib.xml.parser.util.OnParsedInterface;

public class Scanner {
    public final DoubleScanner latitude,longitude,altitude,id;
    public final DateScanner dateTime;

    public final References referencer = new References();
    public final Tags tags = new Tags();

    public OnParsedInterface
            wayParsed	  = OnParsedInterface.NULL,
            routeParsed	  = OnParsedInterface.NULL,
            trackParsed	  = OnParsedInterface.NULL;



    public Scanner(long time) throws SecurityException {
        latitude = new DoubleScanner(6);
        longitude = new DoubleScanner(6);
        altitude = new DoubleScanner(0);
        id = new DoubleScanner(0);
        dateTime = new DateScanner(time);
    }
}
