package ch.bailu.aat.gpx.writer;

import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import ch.bailu.aat.description.FF_GPX;
import ch.bailu.aat.gpx.GpxConstants;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.util.ui.AppConfig;
import ch.bailu.foc.Foc;

public abstract class GpxWriter {


    private final BufferedWriter output;

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
        writeString("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>" +
                    "\n<gpx xmlns=\"http://www.topografix.com/GPX/1/1\"" +
                    "\n    creator=\"");
        writeString(AppConfig.getShortName());
        writeString(" ");
        writeString(AppConfig.getLongName());
        writeString(" ");
        writeString(AppConfig.getVersionName());
        writeString("\" version=\"1.0\"" +
                    "\n    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                    "\n    xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">" +

                    "\n<metadata>");
        writeTimeStamp(timestamp);
        writeString("</metadata>\n");

    }

    protected void writeString(String string) throws IOException {
        output.write(string,0,string.length());
    }

    protected void writeTimeStamp(long time) throws IOException {
        writeString(
                "<" + GpxConstants.QNAME_TIME + ">"
                + f.TIME.format(time) +
                "</" + GpxConstants.QNAME_TIME + ">");
    }



    protected void writeEndElement(String e) throws IOException {
        writeString("</"); writeString(e); writeString(">");
    }

    protected void writeElementEnd() throws IOException{
        writeString("/>");
    }

    protected void writeBeginElementStart(String e) throws IOException {
        writeString("<"); writeString(e);
    }

    protected void writeBeginElementEnd() throws IOException{
        writeString(">");
    }



    protected void writeBeginElement(String e) throws IOException {
        writeBeginElementStart(e); writeBeginElementEnd();
    }

    protected void writeParameter(String pname, String pvalue) throws IOException {
        writeString(" ");
        writeString(pname);
        writeString("=\"");
        writeString(TextUtils.htmlEncode(pvalue));
        writeString("\"");
    }



    protected void writeAttributesGpxStyle(GpxPointInterface tp) throws IOException {
        if (tp.getAttributes().size() > 0) {
            writeBeginElement(GpxConstants.QNAME_EXTENSIONS);

            for(int i=0; i< tp.getAttributes().size(); i++) {
                writeString("\n\t\t");
                writeAttributeGpxStyle(toTag(tp.getAttributes().getSKeyAt(i)),
                        tp.getAttributes().getAt(i));
            }

            writeString("\n\t");
            writeEndElement(GpxConstants.QNAME_EXTENSIONS);
        }
    }

    private String toTag(String key) {
        return key.replace(':', '_');
    }


    protected void writeAttributeGpxStyle(String key, String val) throws IOException {
        writeBeginElement(key);
        writeString(val);
        writeEndElement(key);
    }
}
