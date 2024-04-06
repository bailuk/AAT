package ch.bailu.aat_lib.service.directory;

import java.io.Closeable;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;

public abstract class Iterator implements Closeable {
    public static final Iterator NULL = new Iterator() {
        @Override
        public long getId() {
            return 0;
        }

        @Override
        public GpxInformation getInfo() {
            return GpxInformation.NULL;
        }
    };


    public interface OnCursorChangedListener {
        void onCursorChanged();
    }

    public static final OnCursorChangedListener NULL_LISTENER = () -> {};


    public abstract long getId();
    public int getInfoID() {
        return InfoID.FILE_VIEW;
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


    public abstract GpxInformation getInfo();

    public void query() {}


    @Override
    public void close() {}


    public void setOnCursorChangedListener(OnCursorChangedListener l) {}

}
