package ch.bailu.aat.services.directory;

import android.database.Cursor;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.GpxInformation;

public class GpxInformationDbEntry extends GpxInformation {
    private final Cursor cursor;


    public GpxInformationDbEntry(Cursor c) {
        cursor=c;
    }


    @Override
    public boolean isLoaded() {
        return isValid();
    }


    @Override
    public String getPath() {
        return getString(GpxDbConstants.KEY_PATHNAME);
    }

    @Override
    public String getName() {
        return getString(GpxDbConstants.KEY_FILENAME);
    }

    @Override
    public float getSpeed() {
        return getFloat(GpxDbConstants.KEY_AVG_SPEED);
    }

    @Override
    public float getDistance() {
        return getFloat(GpxDbConstants.KEY_DISTANCE);
    }

    @Override
    public float getMaximumSpeed() {
        return getFloat(GpxDbConstants.KEY_MAX_SPEED);
    }


    @Override
    public long getPause() {
        return getLong(GpxDbConstants.KEY_PAUSE);
    }


    public boolean isValid() {
        return (cursor.getPosition() > -1 && cursor.getPosition() < cursor.getCount());
    }

    private String getString(String key) {
        if (isValid())
            return cursor.getString(cursor.getColumnIndex(key));
        else return "";
    }


    private long getLong(String key) {
        if (isValid())
            return cursor.getLong(cursor.getColumnIndex(key));
        else return 0;
    }


    private float getFloat(String key) {
        if (isValid())
            return cursor.getFloat(cursor.getColumnIndex(key));
        else return 0f;
    }


    @Override
    public long getStartTime() {
        return getLong(GpxDbConstants.KEY_START_TIME);
    }

    @Override
    public long getTimeDelta() {
        return getLong(GpxDbConstants.KEY_TOTAL_TIME);
    }


    @Override
    public long getEndTime() {
        return getLong(GpxDbConstants.KEY_END_TIME);
    }


    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(
                (int)getLong(GpxDbConstants.KEY_NORTH_BOUNDING),
                (int)getLong(GpxDbConstants.KEY_EAST_BOUNDING),
                (int)getLong(GpxDbConstants.KEY_SOUTH_BOUNDING),
                (int)getLong(GpxDbConstants.KEY_WEST_BOUNDING));
    }



    @Override
    public int getType() {
        return (int) getLong(GpxDbConstants.KEY_TYPE_ID);
    }


    @Override
    public int getID() {
        return ID.INFO_ID_FILEVIEW;
    }
}
