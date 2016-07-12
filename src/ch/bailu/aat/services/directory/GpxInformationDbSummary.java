package ch.bailu.aat.services.directory;

import android.database.Cursor;
import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxBigDelta;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.interfaces.GpxBigDeltaInterface;

public class GpxInformationDbSummary extends GpxInformation {
    private final GpxList list;
    
    public GpxInformationDbSummary(Cursor cursor) {
        list = new GpxList(GpxBigDeltaInterface.WAY);
        
        GpxBigDelta summary=new GpxBigDelta();
        GpxInformation entry = new GpxInformationDbEntry(cursor);
        
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
        attr.put("name", entry.getName());
        attr.put("path", entry.getPath());
        
        list.appendToCurrentSegment(point, attr);
    }
    
    
    
    @Override
    public GpxList getGpxList() {
        return list;
    }
    
    public int getID() {
        return GpxInformation.ID.INFO_ID_LIST_SUMMARY;
    }

}
