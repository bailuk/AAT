package ch.bailu.aat.gpx.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import ch.bailu.aat.description.FF_GPX;
import ch.bailu.aat.gpx.GpxConstants;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.util.ui.AppString;
import ch.bailu.util_java.foc.Foc;

public abstract class GpxWriter {


    private BufferedWriter output=null;

    protected final FF_GPX f = FF_GPX.f();


    public GpxWriter(Foc file) throws IOException, SecurityException {
        output = new BufferedWriter(new OutputStreamWriter(file.openW()),8*1024);

    }
    public static GpxWriter factory(Foc file, GpxType type) throws IOException, SecurityException{
        if (type == GpxType.TRACK) {
            return new TrackWriter(file);
        } else if (type == GpxType.ROUTE) {
            return new RouteWriter(file);
        }
        return new WayWriter(file);
    }

    public abstract void writeFooter() throws IOException;
    public abstract void writeSegment() throws IOException;
    public abstract void writeFirstSegment() throws IOException;
    public abstract void writeTrackPoint(GpxPointInterface tp) throws IOException;

    public void close() throws IOException {
        output.close();		
    }


    public void writeHeader(long timestamp) throws IOException {
        writeString("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
        writeString("\n<gpx xmlns=\"http://www.topografix.com/GPX/1/1\"");
        writeString("\n    creator=\"");
        writeString(AppString.getShortName());
        writeString(" ");
        writeString(AppString.getLongName());
        writeString(" ");
        writeString(AppString.getVersionName());
        writeString("\" version=\"1.0\"");
        writeString("\n    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        writeString("\n    xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">");

        writeString("\n<metadata>");
        writeTimeStamp(timestamp);
        writeString("</metadata>\n");

    }

    public void writeString(String string) throws IOException {
        output.write(string,0,string.length());
    }

    public void writeTimeStamp(long time) throws IOException {
        writeString("<time>" + f.TIME.format(time) + "</time>");
    }



    public void writeEndElement(String e) throws IOException {
        writeString("</"); writeString(e); writeString(">");
    }

    public void writeElementEnd() throws IOException{
        writeString("/>");
    }

    public void writeBeginElementStart(String e) throws IOException {
        writeString("<"); writeString(e);
    }

    public void writeBeginElementEnd() throws IOException{
        writeString(">");
    }



    public void writeBeginElement(String e) throws IOException {
        writeBeginElementStart(e); writeBeginElementEnd();
    }

    public void writeParameter(String pname, String pvalue) throws IOException {
        writeString(" ");
        writeString(pname);
        writeString("=\"");
        writeString(pvalue);
        writeString("\"");
    }


    public void writeAttributesGpxStyle(GpxPointInterface tp) throws IOException {
        if (tp.getAttributes().size()>0) {
            writeBeginElement(GpxConstants.QNAME_EXTENSIONS);

            for(int i=0; i< tp.getAttributes().size(); i++) {
                writeString("\n\t\t");
                writeAttributeGpxStyle(tp.getAttributes().getKey(i), tp.getAttributes().getValue(i));
            }

            writeString("\n\t");
            writeEndElement(GpxConstants.QNAME_EXTENSIONS);
        }
    }


    public void writeAttributeGpxStyle(String key, String val) throws IOException {
        writeBeginElement(key);
        writeString(val);
        writeEndElement(key);
    }
/*
    public void writeAttributesTagStyle(GpxPointInterface tp) throws IOException {
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
    */

}
