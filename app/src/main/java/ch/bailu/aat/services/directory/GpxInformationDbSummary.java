package ch.bailu.aat.services.directory;

import android.database.Cursor;

import java.io.File;

import ch.bailu.aat.gpx.GpxAttributesStatic;
import ch.bailu.aat.gpx.GpxBigDelta;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.MaxSpeed;
import ch.bailu.aat.gpx.interfaces.GpxType;

public class GpxInformationDbSummary extends GpxInformation {
    private final GpxList list;
    private final File directory;
    
    public GpxInformationDbSummary(File dir, Cursor cursor) {
        directory = dir;
        list = new GpxList(GpxType.WAY, new MaxSpeed.Raw());
        
        GpxBigDelta summary=new GpxBigDelta(new MaxSpeed.Raw());
        GpxInformation entry = new GpxInformationDbEntry(cursor, dir);
        
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            addEntryToList(entry, cursor);
            summary.updateWithPause(entry);
        }
        setVisibleTrackSegment(summary);
        
        
    }

    private void addEntryToList(GpxInformation entry, Cursor cursor) {
        final GpxPoint point = new GpxPoint(
                entry.getBoundingBox().toBoundingBoxE6().getCenter(),
                0, entry.getTimeStamp());

        list.appendToCurrentSegment(point, GpxAttributesStatic.NULL_ATTRIBUTES);
    }


    @Override
    public String getName() {
        return directory.getName();
    }

    
    @Override
    public String getPath() {
        return directory.getAbsolutePath();
    }

    @Override
    public GpxList getGpxList() {
        return list;
    }
    
    
    @Override
    public boolean isLoaded() {
        return true;
    }
    
    public int getID() {
        return InfoID.LIST_SUMMARY;
    }

}
