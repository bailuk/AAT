package ch.bailu.aat.gpx.writer;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListIterator;


public class GpxListWriter implements  Closeable {


    private GpxListIterator iterator;
    private GpxWriter writer;


    public GpxListWriter(GpxList track, File file) throws IOException  {
        writer = GpxWriter.factory(file, track.getDelta().getType());
        iterator = new GpxListIterator(track);

        writer.writeHeader(System.currentTimeMillis());
    }

    
    @Override
    public void close() throws IOException {
        flushOutput();
        writer.writeFooter();
        writer.close();
    }


    public void flushOutput() throws IOException {
        while (iterator.nextPoint()) {
            if (iterator.isFirstInSegment()) {
                if (iterator.isFirstInTrack()) {
                    writer.writeFirstSegment(iterator.getPoint().getTimeStamp());  
                } else {
                    writer.writeSegment();
                }
            }
            writer.writeTrackPoint(iterator.getPoint());
        }
    }
}
