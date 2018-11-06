package ch.bailu.aat.gpx.writer;

import java.io.IOException;

import ch.bailu.aat.description.FF;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.util_java.foc.Foc;

public class TrackWriter extends GpxWriter {

    public TrackWriter(Foc file) throws IOException, SecurityException{
        super(file);
    }

    @Override
    public void writeHeader(long timestamp) throws IOException, SecurityException {
        super.writeHeader(timestamp);
        writeBeginElement(GpxConstants.QNAME_TRACK);
    }

    @Override
    public void writeFooter() throws IOException {
        writeEndElement(GpxConstants.QNAME_TRACK_SEGMENT);
        writeEndElement(GpxConstants.QNAME_TRACK);
        writeEndElement(GpxConstants.QNAME_GPX);
    }

    @Override
    public void writeTrackPoint(GpxPointInterface tp) throws IOException {
        writeString("\t");
        writeBeginElementStart(GpxConstants.QNAME_TRACK_POINT);
        writeParameter(GpxConstants.QNAME_LATITUDE, f.N6.format(tp.getLatitude()));
        writeParameter(GpxConstants.QNAME_LONGITUDE, f.N6.format(tp.getLongitude()));

        writeBeginElementEnd();

        writeBeginElement(GpxConstants.QNAME_ALTITUDE);
        writeString(f.N.format(tp.getAltitude()));
        writeEndElement(GpxConstants.QNAME_ALTITUDE);

        writeTimeStamp(tp.getTimeStamp()); 

        writeEndElement(GpxConstants.QNAME_TRACK_POINT);
        writeString("\n");
    }

    @Override
    public void writeFirstSegment() throws IOException {
        writeBeginElement(GpxConstants.QNAME_TRACK_SEGMENT);
        writeString("\n");
    }


    @Override
    public void writeSegment() throws IOException {
        writeEndElement(GpxConstants.QNAME_TRACK_SEGMENT);
        writeBeginElement(GpxConstants.QNAME_TRACK_SEGMENT);
        writeString("\n");
    }

}
