package ch.bailu.aat_lib.file.xml.writer

import ch.bailu.aat_lib.file.xml.parser.osm.OsmConstants
import ch.bailu.aat_lib.gpx.GpxConstants
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.foc.Foc
import java.io.IOException

class WayWriterOsmTags(file: Foc) : WayWriter(file) {
    @Throws(IOException::class)
    override fun writeAttributes(tp: GpxPointInterface) {
        writeAttributesTagStyle(tp)
    }


    @Throws(IOException::class)
    private fun writeAttributesTagStyle(tp: GpxPointInterface) {
        //     <tag k="tourism" v="camp_site"/>

        if (tp.getAttributes().size() > 0) {
            writeBeginElement(GpxConstants.QNAME_EXTENSIONS)

            for (i in 0 until tp.getAttributes().size()) {
                writeString("\n\t\t")
                writeBeginElementStart(OsmConstants.T_TAG)
                writeParameter(OsmConstants.A_KEY, tp.getAttributes().getSKeyAt(i))
                writeParameter(OsmConstants.A_VALUE, tp.getAttributes().getAt(i))
                writeElementEnd()
            }

            writeString("\n\t")
            writeEndElement(GpxConstants.QNAME_EXTENSIONS)
        }
    }
}
