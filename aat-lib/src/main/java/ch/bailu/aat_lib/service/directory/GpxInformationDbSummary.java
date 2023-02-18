package ch.bailu.aat_lib.service.directory;


import ch.bailu.aat_lib.gpx.GpxBigDelta;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPoint;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributesNull;
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes;
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.util.sql.DbResultSet;
import ch.bailu.foc.Foc;

public final class GpxInformationDbSummary extends GpxInformation {
    private final GpxList list;
    private final Foc directory;

    private final MaxSpeed maxSpeed = new MaxSpeed.Raw2();


    public GpxInformationDbSummary(Foc dir, DbResultSet cursor) {
        directory = dir;
        list = new GpxList(GpxType.WAY, GpxListAttributes.factoryTrackList());

        GpxBigDelta summary=new GpxBigDelta(GpxListAttributes.factoryTrackList());
        GpxInformation entry = new GpxInformationDbEntry(cursor, dir);

        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            addEntryToList(entry);
            if (hasTimeDelta(entry)) {
                summary.updateWithPause(entry);
            }
        }
        setVisibleTrackSegment(summary);
    }

    private boolean hasTimeDelta(GpxInformation entry) {
        return     entry.getTimeDelta() > 0
                && entry.getStartTime() > 0
                && entry.getEndTime() > entry.getStartTime();
    }

    private void addEntryToList(GpxInformation entry) {
        final GpxPoint point = new GpxPoint(
                entry.getBoundingBox().getCenter(),
                0, entry.getTimeStamp());

        list.appendToCurrentSegment(point, GpxAttributesNull.NULL);
        maxSpeed.add(entry.getSpeed());
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

    @Override
    public GpxAttributes getAttributes() {
        return maxSpeed;
    }
}
