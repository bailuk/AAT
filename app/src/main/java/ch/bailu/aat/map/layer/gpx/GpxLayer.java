package ch.bailu.aat.map.layer.gpx;

import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;

public abstract class GpxLayer implements MapLayerInterface, OnContentUpdatedInterface {
    private final int color;

    private GpxList gpxList=GpxList.NULL_ROUTE;


    public GpxLayer(int c) {
        color = c;
    }

    @Override
    public void drawForeground(MapContext mcontext) {}

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setTrack(info.getGpxList());
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
