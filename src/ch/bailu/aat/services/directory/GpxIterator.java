package ch.bailu.aat.services.directory;

import android.database.Cursor;
import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.GpxBigDelta;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;
import ch.bailu.aat.services.cache.ObjectHandle;



public class GpxIterator extends AbsIterator {
    private Cursor cursor;
    private FileListSummary listSummary;
    private ObjectHandle handle=ObjectHandle.NULL;
    private final CacheService loader;


    public GpxIterator(Cursor c, CacheService fl) {
        loader=fl;

        cursor = c;
        listSummary = new FileListSummary();
    }


    public GpxIterator(Cursor c, CacheService fl, int position) {
        this(c,fl);
        setPosition(position);
    }


    // TODO remove this function
    @Override
    public void setCursor(Cursor c) {
        int position = cursor.getPosition();
        cursor.close();
        cursor = c;

        listSummary = new FileListSummary();

        setPosition(position);
    }



    @Override
    public void setPosition(int index) {
        if (index >= size()) {
            cursor.moveToFirst();
        } else if (index < 0) {
            cursor.moveToLast();
        } else {
            cursor.moveToPosition(index);
        }
    }


    @Override
    public int getPosition() {
        return cursor.getPosition();
    }


    @Override
    public int size() {
        return cursor.getCount();
    }


    @Override
    public boolean isLoaded() {
        ObjectHandle oldHandle = handle;

        handle = loader.getObject( getPath(), new GpxObjectStatic.Factory() );
        oldHandle.free();
        return handle.getSize()>0;
    }


    @Override
    public GpxList getGpxList() {
        if (isLoaded()) 
            return ((GpxObject)handle).getGpxList();
        else return	new GpxList(GpxBigDelta.WAY);
    }



    @Override
    public GpxInformation getListSummary() {
        return listSummary;
    }



    class FileListSummary extends GpxInformation {
        private GpxBigDelta summary;

        public FileListSummary() {
            summary=new GpxBigDelta();

            for (int i=0; i<GpxIterator.this.size(); i++) {
                GpxIterator.this.setPosition(i);
                summary.updateWithPause(GpxIterator.this);
            }
            setVisibleTrackSegment(summary);
        }

        public int getID() {
            return GpxInformation.ID.INFO_ID_LIST_SUMMARY;
        }

    }   

    @Override
    public String getPath() {
        return cursor.getString(cursor.getColumnIndex(GpxDbConstants.KEY_PATHNAME));
    }

    @Override
    public String getName() {
        return cursor.getString(cursor.getColumnIndex(GpxDbConstants.KEY_FILENAME));
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


    private long getLong(String key) {
        return cursor.getLong(cursor.getColumnIndex(key));
    }


    private float getFloat(String key) {
        return cursor.getFloat(cursor.getColumnIndex(key));
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


    @Override
    public void close() {
        handle.free();
        handle = ObjectHandle.NULL;
        cursor.close();
        
    }
}
