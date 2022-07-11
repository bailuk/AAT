package ch.bailu.aat_lib.service.directory;

import java.io.Closeable;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;

public class Iterator implements Closeable {
    public static final Iterator NULL = new Iterator();


    public interface OnCursorChangedListener {
        void onCursorChanged();
    }

    public static final OnCursorChangedListener NULL_LISTENER = () -> {};


    public int getInfoID() {
        return InfoID.FILEVIEW;
    }


    public boolean moveToPrevious() {return false;}


    public boolean moveToNext() {return false;}


    public boolean moveToPosition(int pos) {return false;}


    public int getCount() {
        return 0;
    }


    public int getPosition() {
        return 0;
    }


    public GpxInformation getInfo() {
        return GpxInformation.NULL;
    }


    public void query() {}


    @Override
    public void close() {}


    public void setOnCursorChangedListener(OnCursorChangedListener l) {}

}
