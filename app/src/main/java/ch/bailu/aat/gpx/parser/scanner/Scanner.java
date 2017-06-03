package ch.bailu.aat.gpx.parser.scanner;

import android.util.SparseArray;

import java.io.IOException;
import java.util.ArrayList;

import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.gpx.GpxAttributesStatic;
import ch.bailu.simpleio.foc.Foc;
import ch.bailu.simpleio.io.Stream;
import ch.bailu.simpleio.parser.OnParsedInterface;
import ch.bailu.simpleio.parser.scanner.DateScanner;
import ch.bailu.simpleio.parser.scanner.DoubleScanner;

public class Scanner {
    public final Stream stream;
    public final DoubleScanner latitude,longitude,altitude,id;
    public final DateScanner dateTime;
    public final StringBuilder builder=new StringBuilder();



    public OnParsedInterface
            wayParsed	  = OnParsedInterface.NULL,
            routeParsed	  = OnParsedInterface.NULL,
            trackParsed	  = OnParsedInterface.NULL,
            parsed        = OnParsedInterface.NULL;

    public final SparseArray<LatLongE6> nodeMap = new SparseArray<>(50);
    public final ArrayList<GpxAttributesStatic.Tag> tagList = new ArrayList<>();

    public Scanner(Foc in) throws IOException, SecurityException {
        stream = new Stream(in);

        latitude = new DoubleScanner(stream,6);
        longitude = new DoubleScanner(stream,6);
        altitude = new DoubleScanner(stream,0);
        id = new DoubleScanner(stream,0);
        dateTime = new DateScanner(stream, in.lastModified());
    }
}
