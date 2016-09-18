package ch.bailu.aat.gpx.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;

import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.helpers.AppTheme;

public abstract class GpxWriter {
    private BufferedWriter output=null;


    public GpxWriter(File file) throws FileNotFoundException {
        output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)),8*1024);

    }
    public static GpxWriter factory(File file, int type) throws FileNotFoundException {
        if (type == GpxType.TRK) {
            return new TrackWriter(file);
        } else if (type == GpxType.RTE) {
            return new RouteWriter(file);
        }
        return new WayWriter(file);
    }

    public abstract void writeFooter() throws IOException;
    public abstract void writeSegment() throws IOException;
    public abstract void writeFirstSegment(long timestamp) throws IOException; 
    public abstract void writeTrackPoint(GpxPointInterface tp) throws IOException;

    public void close() throws IOException {
        output.close();		
    }


    public void writeHeader(long timestamp) throws IOException {
        writeString("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
        writeString("\n<gpx xmlns=\"http://www.topografix.com/GPX/1/1\"");
        writeString("\n    creator=\"");
        writeString(AppTheme.getAppShortName());
        writeString(" ");
        writeString(AppTheme.getAppFullName()); 
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
        writeString(String.format((Locale)null,"<time>%tFT%tT.%tL</time>", time,time,time)); 
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
