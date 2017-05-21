package ch.bailu.aat.gpx.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.dem.tile.ElevationProvider;

public class WayWriter extends GpxWriter {

    public WayWriter(File file) throws FileNotFoundException {
        super(file);
    }

    @Override
    public void writeFooter() throws IOException {
        writeEndElement(GpxConstants.QNAME_GPX);
    }

    @Override
    public void writeSegment() throws IOException {
    }

    @Override
    public void writeFirstSegment() throws IOException {
    }

    @Override
    public void writeTrackPoint(GpxPointInterface tp) throws IOException {
        writeString("\t");
        writeBeginElementStart(GpxConstants.QNAME_WAY_POINT);
        writeParameter(GpxConstants.QNAME_LATITUDE, String.format((Locale)null, "%.6f", tp.getLatitude()));
        writeParameter(GpxConstants.QNAME_LONGITUDE, String.format((Locale)null, "%.6f", tp.getLongitude()));

        writeBeginElementEnd();

        if (tp.getAltitude() != ElevationProvider.NULL_ALTITUDE) {
            writeBeginElement(GpxConstants.QNAME_ALTITUDE);
            writeString(String.format((Locale)null, "%d",(int)tp.getAltitude()));
            writeEndElement(GpxConstants.QNAME_ALTITUDE);
        }

        
        writeEndElement(GpxConstants.QNAME_WAY_POINT);
        writeString("\n");
    }

}
