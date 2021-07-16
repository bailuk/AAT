package ch.bailu.aat.gpx.writer;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxConstants;
import ch.bailu.aat.gpx.OsmConstants;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.foc.Foc;

public class WayWriterOsmTags extends WayWriter {
    public WayWriterOsmTags(Foc file) throws IOException {
        super(file);
    }


    @Override
    public void writeAttributes(GpxPointInterface tp) throws IOException {
        writeAttributesTagStyle(tp);
    }


    protected void writeAttributesTagStyle(GpxPointInterface tp) throws IOException {
        //     <tag k="tourism" v="camp_site"/>

        if (tp.getAttributes().size()>0) {
            writeBeginElement(GpxConstants.QNAME_EXTENSIONS);

            for(int i=0; i< tp.getAttributes().size(); i++) {
                writeString("\n\t\t");
                writeBeginElementStart(OsmConstants.T_TAG);
                writeParameter(OsmConstants.A_KEY, tp.getAttributes().getSKeyAt(i));
                writeParameter(OsmConstants.A_VALUE, tp.getAttributes().getAt(i));
                writeElementEnd();
            }

            writeString("\n\t");
            writeEndElement(GpxConstants.QNAME_EXTENSIONS);
        }
    }

}
