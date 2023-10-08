package ch.bailu.aat_lib.map.layer.gpx;

import ch.bailu.aat_lib.map.MapColor;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;

public abstract class GpxLayer implements MapLayerInterface, OnContentUpdatedInterface {
    private int color;
    private GpxList gpxList= GpxList.NULL_ROUTE;


    @Override
    public void drawForeground(MapContext mcontext) {}

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setTrack(info.getGpxList());
        color = MapColor.getColorFromIID(iid);
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

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}
}
