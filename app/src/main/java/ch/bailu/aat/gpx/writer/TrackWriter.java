package ch.bailu.aat.gpx.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import ch.bailu.aat.gpx.interfaces.GpxPointInterface;

public class TrackWriter extends GpxWriter {

    public TrackWriter(File file) throws FileNotFoundException {
        super(file);
    }

    @Override
    public void writeHeader(long timestamp) throws IOException {
        super.writeHeader(timestamp);
        writeBeginElement(QNAME_TRACK);
    }

    @Override
    public void writeFooter() throws IOException {
        writeEndElement(QNAME_TRACK_SEGMENT);
        writeEndElement(QNAME_TRACK);
        writeEndElement(QNAME_GPX);
    }

    @Override
    public void writeTrackPoint(GpxPointInterface tp) throws IOException {
        writeString("\t");
        writeBeginElementStart(QNAME_TRACK_POINT);
        writeParameter(QNAME_LATITUDE, String.format((Locale)null, "%.6f", tp.getLatitude()));
        writeParameter(QNAME_LONGITUDE, String.format((Locale)null, "%.6f", tp.getLongitude()));

        writeBeginElementEnd();

        writeBeginElement(QNAME_ALTITUDE);
        writeString(String.format((Locale)null, "%d",(int)tp.getAltitude()));
        writeEndElement(QNAME_ALTITUDE);

        writeTimeStamp(tp.getTimeStamp()); 

        writeEndElement(QNAME_TRACK_POINT);
        writeString("\n");
    }

    @Override
    public void writeFirstSegment(long timestamp) throws IOException {
        writeBeginElement(QNAME_TRACK_SEGMENT);
        writeString("\n");
    }


    @Override
    public void writeSegment() throws IOException {
        writeEndElement(QNAME_TRACK_SEGMENT);
        writeBeginElement(QNAME_TRACK_SEGMENT);
        writeString("\n");
    }

}
