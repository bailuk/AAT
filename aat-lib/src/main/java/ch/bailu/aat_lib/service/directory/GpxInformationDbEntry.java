package ch.bailu.aat_lib.service.directory;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.gpx.information.GpxInformation;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration;
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
    public boolean getLoaded() {
        return false;//isSupported();
    }


    @Override
    public Foc getFile() {
        String name = getString(GpxDbConfiguration.KEY_FILENAME);
        return parent.child(name);
    }

    @Override
    public float getSpeed() {
        return getFloat(GpxDbConfiguration.KEY_AVG_SPEED);
    }

    @Override
    public float getDistance() {
        return getFloat(GpxDbConfiguration.KEY_DISTANCE);
    }

    @Override
    public long getPause() {
        return getLong(GpxDbConfiguration.KEY_PAUSE);
    }

    public boolean isValid() {
        return (!cursor.isClosed() &&
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
        return getLong(GpxDbConfiguration.KEY_START_TIME);
    }

    @Override
    public long getTimeDelta() {
        return getLong(GpxDbConfiguration.KEY_TOTAL_TIME);
    }

    @Override
    public long getEndTime() {
        return getLong(GpxDbConfiguration.KEY_END_TIME);
    }

    @Override
    public BoundingBoxE6 getBoundingBox() {
        return new BoundingBoxE6(
                (int)getLong(GpxDbConfiguration.KEY_NORTH_BOUNDING),
                (int)getLong(GpxDbConfiguration.KEY_EAST_BOUNDING),
                (int)getLong(GpxDbConfiguration.KEY_SOUTH_BOUNDING),
                (int)getLong(GpxDbConfiguration.KEY_WEST_BOUNDING));
    }

    @Override
    public GpxType getType() {
        int id = (int) getLong(GpxDbConfiguration.KEY_TYPE_ID);
        return GpxType.fromInteger(id);
    }
}
