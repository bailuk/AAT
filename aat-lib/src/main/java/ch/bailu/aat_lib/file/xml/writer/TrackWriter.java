package ch.bailu.aat_lib.file.xml.writer;

import java.io.IOException;

import ch.bailu.aat_lib.gpx.GpxConstants;
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;
import ch.bailu.foc.Foc;

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
        writeParameter(GpxConstants.QNAME_LATITUDE, f.decimal6.format(tp.getLatitude()));
        writeParameter(GpxConstants.QNAME_LONGITUDE, f.decimal6.format(tp.getLongitude()));

        writeBeginElementEnd();

        writeBeginElement(GpxConstants.QNAME_ALTITUDE);
        writeString(f.decimal2.format(tp.getAltitude()));
        writeEndElement(GpxConstants.QNAME_ALTITUDE);

        writeTimeStamp(tp.getTimeStamp());
        writeAttributesGpxStyle(tp);

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
