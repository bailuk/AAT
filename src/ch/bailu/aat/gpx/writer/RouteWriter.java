package ch.bailu.aat.gpx.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.srtm.SRTM;

public class RouteWriter extends GpxWriter {

    public RouteWriter(File file) throws FileNotFoundException {
        super(file);
    }

    @Override
    public void writeHeader(long timestamp) throws IOException {
        super.writeHeader(timestamp);
        writeBeginElement(QNAME_ROUTE);
    }

    @Override
    public void writeFooter() throws IOException {
        writeEndElement(QNAME_ROUTE);
        writeEndElement(QNAME_GPX);
    }

    @Override
    public void writeSegment() throws IOException {}

    @Override
    public void writeFirstSegment(long timestamp) throws IOException {}

    @Override
    public void writeTrackPoint(GpxPointInterface tp) throws IOException {
        writeString("\t");
        writeBeginElementStart(QNAME_ROUTE_POINT);
            writeParameter(QNAME_LATITUDE, String.format((Locale)null, "%.6f", tp.getLatitude()));
            writeParameter(QNAME_LONGITUDE, String.format((Locale)null, "%.6f", tp.getLongitude()));
        writeBeginElementEnd();
        
        if (tp.getAltitude() != SRTM.NULL_ALTITUDE) {
            writeBeginElement(QNAME_ALTITUDE);
            writeString(String.format((Locale)null, "%d",(int)tp.getAltitude()));
            writeEndElement(QNAME_ALTITUDE);
        }
        
        writeEndElement(QNAME_ROUTE_POINT);
        writeString("\n");
    }
}
