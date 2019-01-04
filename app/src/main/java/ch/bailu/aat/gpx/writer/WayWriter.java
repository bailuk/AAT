package ch.bailu.aat.gpx.writer;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxConstants;
import ch.bailu.aat.gpx.OsmConstants;
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
        writeParameter(GpxConstants.QNAME_LATITUDE, f.N6.format(tp.getLatitude()));
        writeParameter(GpxConstants.QNAME_LONGITUDE, f.N6.format(tp.getLongitude()));

        writeBeginElementEnd();

        writeAltitude(tp);
        writeAttributes(tp);

        writeEndElement(GpxConstants.QNAME_WAY_POINT);
        writeString("\n");
    }



    private void writeAltitude(GpxPointInterface tp) throws IOException {
        if (tp.getAltitude() != ElevationProvider.NULL_ALTITUDE) {
            writeBeginElement(GpxConstants.QNAME_ALTITUDE);
            writeString(f.N.format(tp.getAltitude()));
            writeEndElement(GpxConstants.QNAME_ALTITUDE);
        }
    }

    private void writeAttributes(GpxPointInterface tp) throws IOException {
        if (tp.getAttributes().size()>0) {
            writeBeginElement(GpxConstants.QNAME_EXTENSIONS);

            for(int i=0; i< tp.getAttributes().size(); i++) {
                writeString("\n\t\t");
                writeBeginElementStart(OsmConstants.T_TAG);
                writeParameter(OsmConstants.A_KEY, tp.getAttributes().getKey(i));
                writeParameter(OsmConstants.A_VALUE, tp.getAttributes().getValue(i));
                writeElementEnd();
            }

            writeString("\n\t");
            writeEndElement(GpxConstants.QNAME_EXTENSIONS);
        }
    }

}
