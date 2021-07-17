package ch.bailu.aat_lib.xml.writer;

import java.io.IOException;

import ch.bailu.aat_lib.gpx.GpxConstants;
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat_lib.service.elevation.ElevationProvider;
import ch.bailu.foc.Foc;

public class WayWriter extends GpxWriter {

    public WayWriter(Foc file) throws IOException {
        super(file);
    }

    @Override
    public void writeFooter() throws IOException {
        writeEndElement(GpxConstants.QNAME_GPX);
    }

    @Override
    public void writeSegment() {
    }

    @Override
    public void writeFirstSegment() {
    }

    @Override
    public void writeTrackPoint(GpxPointInterface tp) throws IOException {
        writeString("\t");
        writeBeginElementStart(GpxConstants.QNAME_WAY_POINT);
        writeParameter(GpxConstants.QNAME_LATITUDE, f.N6.format(tp.getLatitude()));
        writeParameter(GpxConstants.QNAME_LONGITUDE, f.N6.format(tp.getLongitude()));

        writeBeginElementEnd();

        writeAltitude(tp);
        writeAttributes(tp);

        writeEndElement(GpxConstants.QNAME_WAY_POINT);
        writeString("\n");
    }


    public void writeAttributes(GpxPointInterface tp) throws IOException {
        writeAttributesGpxStyle(tp);
    }



    private void writeAltitude(GpxPointInterface tp) throws IOException {
        if (tp.getAltitude() != ElevationProvider.NULL_ALTITUDE) {
            writeBeginElement(GpxConstants.QNAME_ALTITUDE);
            writeString(f.N.format(tp.getAltitude()));
            writeEndElement(GpxConstants.QNAME_ALTITUDE);
        }
    }


}
