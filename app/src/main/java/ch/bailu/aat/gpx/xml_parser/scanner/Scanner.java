package ch.bailu.aat.gpx.xml_parser.scanner;

import android.util.SparseArray;

import java.util.ArrayList;

import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.gpx.GpxAttributesStatic;
import ch.bailu.util_java.parser.OnParsedInterface;
import ch.bailu.util_java.parser.scanner.DateScanner;
import ch.bailu.util_java.parser.scanner.DoubleScanner;

public class Scanner {
    public final DoubleScanner latitude,longitude,altitude,id;
    public final DateScanner dateTime;


    public OnParsedInterface
            wayParsed	  = OnParsedInterface.NULL,
            routeParsed	  = OnParsedInterface.NULL,
            trackParsed	  = OnParsedInterface.NULL,
            currentParsed = OnParsedInterface.NULL;

    public final SparseArray<LatLongE6> nodeMap = new SparseArray<>(50);
    public final ArrayList<GpxAttributesStatic.Tag> tagList = new ArrayList<>();

    public Scanner(long time) throws SecurityException {
        latitude = new DoubleScanner(6);
        longitude = new DoubleScanner(6);
        altitude = new DoubleScanner(0);
        id = new DoubleScanner(0);
        dateTime = new DateScanner(time);
    }
}
