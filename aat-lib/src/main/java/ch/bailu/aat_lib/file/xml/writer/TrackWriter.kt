package ch.bailu.aat_lib.file.xml.writer

import ch.bailu.aat_lib.gpx.GpxConstants
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.foc.Foc
import java.io.IOException

class TrackWriter(file: Foc) : GpxWriter(file) {
    @Throws(IOException::class, SecurityException::class)
    override fun writeHeader(timestamp: Long) {
        super.writeHeader(timestamp)
        writeBeginElement(GpxConstants.QNAME_TRACK)
    }

    @Throws(IOException::class)
    override fun writeFooter() {
        writeEndElement(GpxConstants.QNAME_TRACK_SEGMENT)
        writeEndElement(GpxConstants.QNAME_TRACK)
        writeEndElement(GpxConstants.QNAME_GPX)
    }

    @Throws(IOException::class)
    override fun writeTrackPoint(tp: GpxPointInterface) {
        writeString("\t")
        writeBeginElementStart(GpxConstants.QNAME_TRACK_POINT)
        writeParameter(GpxConstants.QNAME_LATITUDE, f.decimal6.format(tp.getLatitude()))
        writeParameter(GpxConstants.QNAME_LONGITUDE, f.decimal6.format(tp.getLongitude()))

        writeBeginElementEnd()

        writeBeginElement(GpxConstants.QNAME_ALTITUDE)
        writeString(f.decimal1.format(tp.getAltitude().toDouble()))
        writeEndElement(GpxConstants.QNAME_ALTITUDE)

        writeTimeStamp(tp.getTimeStamp())
        writeAttributesGpxStyle(tp)

        writeEndElement(GpxConstants.QNAME_TRACK_POINT)
        writeString("\n")
    }

    @Throws(IOException::class)
    override fun writeFirstSegment() {
        writeBeginElement(GpxConstants.QNAME_TRACK_SEGMENT)
        writeString("\n")
    }


    @Throws(IOException::class)
    override fun writeSegment() {
        writeEndElement(GpxConstants.QNAME_TRACK_SEGMENT)
        writeBeginElement(GpxConstants.QNAME_TRACK_SEGMENT)
        writeString("\n")
    }
}
