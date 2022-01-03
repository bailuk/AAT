package ch.bailu.aat_lib.service.directory;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.util.sql.DbResultSet;
import ch.bailu.foc.Foc;

public final class GpxInformationDbEntry extends GpxInformation {
    private final DbResultSet cursor;
    private final Foc parent;

    public GpxInformationDbEntry(DbResultSet c, Foc p) {
        parent = p;
        cursor = c;
    }


    @Override
    public boolean isLoaded() {
        return false;//isSupported();
    }


    @Override
    public Foc getFile() {
        String name = getString(GpxDbConstants.KEY_FILENAME);
        return parent.child(name);
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
    public long getPause() {
        return getLong(GpxDbConstants.KEY_PAUSE);
    }


    public boolean isValid() {
        return (cursor.isClosed() == false &&
                cursor.getPosition() > -1 &&
                cursor.getPosition() < cursor.getCount());
    }

    private String getString(String key) {
        if (isValid()) {
            return cursor.getString(key);
        }
        return "";
    }


    private long getLong(String key) {
        if (isValid()) {
            return cursor.getLong(key);
        }
        return 0;
    }


    private float getFloat(String key) {
        if (isValid()) {
            return cursor.getFloat(key);
        }
        return 0f;
    }


    @Override
    public long getTimeStamp() { return getStartTime();}


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
    public BoundingBoxE6 getBoundingBox() {
        return new BoundingBoxE6(
                (int)getLong(GpxDbConstants.KEY_NORTH_BOUNDING),
                (int)getLong(GpxDbConstants.KEY_EAST_BOUNDING),
                (int)getLong(GpxDbConstants.KEY_SOUTH_BOUNDING),
                (int)getLong(GpxDbConstants.KEY_WEST_BOUNDING));
    }



    @Override
    public GpxType getType() {
        int id = (int) getLong(GpxDbConstants.KEY_TYPE_ID);
        return GpxType.fromInteger(id);
    }

}
