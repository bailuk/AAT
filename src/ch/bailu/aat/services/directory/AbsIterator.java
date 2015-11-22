package ch.bailu.aat.services.directory;

import android.database.Cursor;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.CleanUp;

public abstract class AbsIterator extends GpxInformation implements CleanUp{

    public abstract void setCursor(Cursor c);

    public abstract void setPosition(int index);
    public abstract int getPosition();


    public abstract int size();


    public boolean isEmpty() {
        return size() == 0;
    }

    public abstract GpxInformation getListSummary();


    public static final AbsIterator NULL_ITERATOR = new AbsIterator() {

        @Override
        public void setCursor(Cursor c) {}

        @Override
        public void setPosition(int index) {}

        @Override
        public int getPosition() {
            return 0;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public GpxInformation getListSummary() {
            return GpxInformation.NULL;
        }

        @Override
        public void cleanUp() {

        }

    };

}
