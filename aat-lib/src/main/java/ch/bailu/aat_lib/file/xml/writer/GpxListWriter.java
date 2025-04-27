package ch.bailu.aat_lib.file.xml.writer;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxListIterator;
import ch.bailu.foc.Foc;

public class GpxListWriter implements Closeable {


    private final GpxListIterator iterator;
    private final GpxWriter writer;


    public GpxListWriter(GpxList track, Foc file) throws IOException, SecurityException  {
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
                    writer.writeFirstSegment();
                } else {
                    writer.writeSegment();
                }
            }
            writer.writeTrackPoint(iterator.getPoint());
        }
    }
}
