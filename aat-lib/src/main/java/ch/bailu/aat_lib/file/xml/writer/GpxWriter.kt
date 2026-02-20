package ch.bailu.aat_lib.file.xml.writer

import ch.bailu.aat_lib.app.AppConfig.Companion.getInstance
import ch.bailu.aat_lib.description.FormatWrite
import ch.bailu.aat_lib.description.FormatWrite.Companion.f
import ch.bailu.aat_lib.gpx.GpxConstants
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.lib.xml.XmlEscaper
import ch.bailu.foc.Foc
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter

abstract class GpxWriter(file: Foc) {
    private val output =
        BufferedWriter(OutputStreamWriter(file.openW()), 8 * 1024)

    private val xmlEscaper = XmlEscaper()

    @JvmField
    protected val f: FormatWrite = f()

    @Throws(IOException::class)
    abstract fun writeFooter()

    @Throws(IOException::class)
    abstract fun writeSegment()

    @Throws(IOException::class)
    abstract fun writeFirstSegment()

    @Throws(IOException::class)
    abstract fun writeTrackPoint(tp: GpxPointInterface)

    /**
     * Flush internal buffers to the output file.
     */
    @Throws(IOException::class)
    fun flush() {
        output.flush()
    }

    @Throws(IOException::class)
    fun close() {
        output.close()
    }

    @Throws(IOException::class)
    open fun writeHeader(timestamp: Long) {
        writeString("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n")
        writeString("<gpx xmlns=\"http://www.topografix.com/GPX/1/1\"\n")
        writeString("    creator=\"${getInstance().appName} ${getInstance().appLongName} ${getInstance().appVersionName}\" version=\"1.1\"\n")
        writeString("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n")
        writeString("    xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">\n")
        writeString("<metadata>")
        writeTimeStamp(timestamp)
        writeString("</metadata>\n")
    }

    @Throws(IOException::class)
    protected fun writeRawChar(ch: Char) {
        output.append(ch)
    }

    @Throws(IOException::class)
    protected fun writeString(string: String) {
        output.write(string)
    }

    @Throws(IOException::class)
    protected fun writeTimeStamp(time: Long) {
        writeBeginElement(GpxConstants.QNAME_TIME);
        writeString(f.dateFormat.format(time));
        writeEndElement(GpxConstants.QNAME_TIME);
    }

    @Throws(IOException::class)
    protected fun writeEndElement(e: String) {
        writeString("</")
        writeString(e)
        writeRawChar('>')
    }

    @Throws(IOException::class)
    protected fun writeElementEnd() {
        writeString("/>")
    }

    @Throws(IOException::class)
    protected fun writeBeginElementStart(e: String) {
        writeRawChar('<')
        writeString(e)
    }

    @Throws(IOException::class)
    protected fun writeBeginElementEnd() {
        writeRawChar('>')
    }

    @Throws(IOException::class)
    protected fun writeBeginElement(e: String) {
        writeBeginElementStart(e)
        writeBeginElementEnd()
    }

    @Throws(IOException::class)
    protected fun writeParameter(parameterName: String, parameterValue: String) {
        writeRawChar(' ')
        writeString(parameterName)
        writeString("=\"")
        writeString(xmlEscaper.escape(parameterValue))
        writeRawChar('"')
    }

    @Throws(IOException::class)
    protected fun writeAttributesGpxStyle(tp: GpxPointInterface) {
        if (tp.getAttributes().size() > 0) {
            writeBeginElement(GpxConstants.QNAME_EXTENSIONS)

            for (i in 0 until tp.getAttributes().size()) {
                writeString("\n\t\t")
                writeAttributeGpxStyle(
                    toTag(tp.getAttributes().getSKeyAt(i)),
                    tp.getAttributes().getAt(i)
                )
            }

            writeString("\n\t")
            writeEndElement(GpxConstants.QNAME_EXTENSIONS)
        }
    }

    private fun toTag(key: String): String {
        return key.replace(':', '_')
    }

    @Throws(IOException::class)
    protected fun writeAttributeGpxStyle(key: String, value: String) {
        writeBeginElement(key)
        writeString(value)
        writeEndElement(key)
    }

    companion object {
        @JvmStatic
        @Throws(IOException::class, SecurityException::class)
        fun factory(file: Foc, type: GpxType): GpxWriter {
            if (type == GpxType.TRACK) {
                return TrackWriter(file)
            } else if (type == GpxType.ROUTE) {
                return RouteWriter(file)
            }
            return WayWriter(file)
        }
    }
}
