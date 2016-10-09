package ch.bailu.aat.views.map.overlay.gpx;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.OsmOverlay;


public abstract class GpxOverlay extends OsmOverlay {
    private final int ID;
    private final int color;

    private GpxList gpxList=GpxList.NULL_ROUTE;

    public GpxOverlay(AbsOsmView osmPreview, int id,  int c) {
        super(osmPreview);

        color = c;
        ID = id;
    }

    @Override
    public void onContentUpdated(GpxInformation info) {
        if (info.getID() == ID) {
            setTrack(info.getGpxList());
        }
    }

    private void setTrack(GpxList gpx) {
        if (gpx == null) gpxList = GpxList.NULL_ROUTE;
        else gpxList = gpx;
    }


    public GpxList getGpxList() {
        return gpxList;
    }


    public int getColor() {
        return color;
    }


}
