package ch.bailu.aat.gpx.writer;

import java.io.IOException;
import java.util.Locale;

import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.dem.tile.ElevationProvider;
import ch.bailu.simpleio.foc.Foc;

public class RouteWriter extends GpxWriter {

    public RouteWriter(Foc file) throws IOException {
        super(file);
    }

    @Override
    public void writeHeader(long timestamp) throws IOException {
        super.writeHeader(timestamp);
        writeBeginElement(GpxConstants.QNAME_ROUTE);
    }

    @Override
    public void writeFooter() throws IOException {
        writeEndElement(GpxConstants.QNAME_ROUTE);
        writeEndElement(GpxConstants.QNAME_GPX);
    }

    @Override
    public void writeSegment() throws IOException {}

    @Override
    public void writeFirstSegment() throws IOException {}

    @Override
    public void writeTrackPoint(GpxPointInterface tp) throws IOException {
        writeString("\t");
        writeBeginElementStart(GpxConstants.QNAME_ROUTE_POINT);
            writeParameter(GpxConstants.QNAME_LATITUDE, String.format((Locale)null, "%.6f", tp.getLatitude()));
            writeParameter(GpxConstants.QNAME_LONGITUDE, String.format((Locale)null, "%.6f", tp.getLongitude()));
        writeBeginElementEnd();
        
        if (tp.getAltitude() != ElevationProvider.NULL_ALTITUDE) {
            writeBeginElement(GpxConstants.QNAME_ALTITUDE);
            writeString(String.format((Locale)null, "%d",(int)tp.getAltitude()));
            writeEndElement(GpxConstants.QNAME_ALTITUDE);
        }
        
        writeEndElement(GpxConstants.QNAME_ROUTE_POINT);
        writeString("\n");
    }
}
