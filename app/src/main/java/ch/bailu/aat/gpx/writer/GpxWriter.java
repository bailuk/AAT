package ch.bailu.aat.gpx.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.simpleio.foc.Foc;

public abstract class GpxWriter {
    private final static TimeZone UTC = TimeZone.getTimeZone("UTC");
    private final static DateFormat TIME_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


    private BufferedWriter output=null;


    public GpxWriter(Foc file) throws IOException, SecurityException {
        output = new BufferedWriter(new OutputStreamWriter(file.openW()),8*1024);
        TIME_FORMAT.setTimeZone(UTC);

    }
    public static GpxWriter factory(Foc file, int type) throws IOException, SecurityException{
        if (type == GpxType.TRK) {
            return new TrackWriter(file);
        } else if (type == GpxType.RTE) {
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
        writeString(AppTheme.APP_SHORT_NAME);
        writeString(" ");
        writeString(AppTheme.APP_LONG_NAME);
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
        writeString("<time>" + TIME_FORMAT.format(time) + "</time>");
    }



    public void writeEndElement(String e) throws IOException {
        writeString("</"); writeString(e); writeString(">");
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
}
