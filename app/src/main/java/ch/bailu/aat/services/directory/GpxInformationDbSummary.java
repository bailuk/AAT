package ch.bailu.aat.services.directory;

import android.database.Cursor;

import ch.bailu.aat.gpx.GpxListAttributes;
import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.GpxBigDelta;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.util_java.foc.Foc;

public class GpxInformationDbSummary extends GpxInformation {
    private final GpxList list;
    private final Foc directory;
    
    public GpxInformationDbSummary(Foc dir, Cursor cursor) {
        directory = dir;
        list = new GpxList(GpxType.WAY, GpxListAttributes.factoryTrackList());
        
        GpxBigDelta summary=new GpxBigDelta(GpxListAttributes.factoryTrackList());
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
                entry.getBoundingBox().getCenter(),
                0, entry.getTimeStamp());

        list.appendToCurrentSegment(point, GpxAttributes.NULL);
    }



    @Override
    public Foc getFile() {
        return directory;
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
