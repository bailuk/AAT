package ch.bailu.aat.gpx.writer;

import java.io.IOException;

import ch.bailu.aat.description.FF;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.dem.tile.ElevationProvider;
import ch.bailu.util_java.foc.Foc;

public class WayWriter extends GpxWriter {

    public WayWriter(Foc file) throws IOException {
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
        writeParameter(GpxConstants.QNAME_LATITUDE, FF.N_6.format(tp.getLatitude()));
        writeParameter(GpxConstants.QNAME_LONGITUDE, FF.N_6.format(tp.getLongitude()));

        writeBeginElementEnd();

        if (tp.getAltitude() != ElevationProvider.NULL_ALTITUDE) {
            writeBeginElement(GpxConstants.QNAME_ALTITUDE);
            writeString(FF.N.format(tp.getAltitude()));
            writeEndElement(GpxConstants.QNAME_ALTITUDE);
        }

        
        writeEndElement(GpxConstants.QNAME_WAY_POINT);
        writeString("\n");
    }

}
