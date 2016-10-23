package ch.bailu.aat.services.directory;

import android.database.Cursor;

import java.io.File;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxBigDelta;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.interfaces.GpxType;

public class GpxInformationDbSummary extends GpxInformation {
    private final GpxList list;
    private final File directory;
    
    public GpxInformationDbSummary(File dir, Cursor cursor) {
        directory = dir;
        list = new GpxList(GpxType.WAY);
        
        GpxBigDelta summary=new GpxBigDelta();
        GpxInformation entry = new GpxInformationDbEntry(cursor, dir);
        
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            addEntryToList(entry);
            summary.updateWithPause(entry);
        }
        setVisibleTrackSegment(summary);
        
        
    }
    

    private void addEntryToList(GpxInformation entry) {
        final GpxPoint point = new GpxPoint(
                entry.getBoundingBox().toBoundingBoxE6().getCenter(), 
                0, entry.getTimeStamp());

        final GpxAttributes attr = new GpxAttributes();
        final File file = new File(entry.getPath());
        attr.put("name", file.getName());
        attr.put("path", file.getPath());
        
        list.appendToCurrentSegment(point, attr);
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
