package ch.bailu.aat_lib.file.xml.writer

import ch.bailu.aat_lib.gpx.GpxConstants
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.service.elevation.ElevationProvider
import ch.bailu.foc.Foc
import java.io.IOException

class RouteWriter(file: Foc) : GpxWriter(file) {
    @Throws(IOException::class)
    override fun writeHeader(timestamp: Long) {
        super.writeHeader(timestamp)
        writeBeginElement(GpxConstants.QNAME_ROUTE)
    }

    @Throws(IOException::class)
    override fun writeFooter() {
        writeEndElement(GpxConstants.QNAME_ROUTE)
        writeEndElement(GpxConstants.QNAME_GPX)
    }

    override fun writeSegment() {}

    override fun writeFirstSegment() {}

    @Throws(IOException::class)
    override fun writeTrackPoint(tp: GpxPointInterface) {
        writeString("\t")
        writeBeginElementStart(GpxConstants.QNAME_ROUTE_POINT)
        writeParameter(GpxConstants.QNAME_LATITUDE, f.decimal6.format(tp.getLatitude()))
        writeParameter(GpxConstants.QNAME_LONGITUDE, f.decimal6.format(tp.getLongitude()))
        writeBeginElementEnd()

        if (tp.getAltitude() != ElevationProvider.NULL_ALTITUDE) {
            writeBeginElement(GpxConstants.QNAME_ALTITUDE)
            writeString(f.decimal1.format(tp.getAltitude().toDouble()))
            writeEndElement(GpxConstants.QNAME_ALTITUDE)
        }

        writeEndElement(GpxConstants.QNAME_ROUTE_POINT)
        writeString("\n")
    }
}
