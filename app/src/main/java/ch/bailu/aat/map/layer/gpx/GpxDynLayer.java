package ch.bailu.aat.map.layer.gpx;

import android.content.SharedPreferences;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxInformationCache;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.preferences.SolidLegend;

public class GpxDynLayer implements MapLayerInterface, OnContentUpdatedInterface {
    private final GpxInformationCache infoCache = new GpxInformationCache();

    private GpxLayer gpxOverlay;
    private GpxLayer legendOverlay;

    private final SolidLegend slegend;

    private final MapContext mcontext;
    private final int color;



    public GpxDynLayer(MapContext mc, int c) {
        mcontext = mc;
        color = c;
        slegend = new SolidLegend(mcontext.getContext(), mcontext.getSolidKey());

        createLegendOverlay();
        createGpxOverlay();
    }


    public GpxDynLayer(MapContext mc,
                         DispatcherInterface dispatcher, int iid) {
        this(mc, -1);
        dispatcher.addTarget(this, iid);
    }


    @Override
    public void draw(MapContext mcontext) {
        gpxOverlay.draw(mcontext);
        legendOverlay.draw(mcontext);
    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        return false;
    }

    private int type = GpxType.NONE;

    @Override
    public void onContentUpdated(int iid, GpxInformation i) {
        infoCache.set(iid, i);

        if (type != toType(i)) {
            type = toType(i);

            createGpxOverlay();
            createLegendOverlay();
        }

        infoCache.letUpdate(gpxOverlay);
        infoCache.letUpdate(legendOverlay);

        mcontext.getMapView().requestRedraw();
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (slegend.hasKey(key)) {
            createLegendOverlay();
            infoCache.letUpdate(legendOverlay);
            mcontext.getMapView().requestRedraw();
        }
    }

    private static int toType(GpxInformation i) {
        if (i != null && i.getGpxList() != null) {
            return i.getGpxList().getDelta().getType();
        }
        return GpxType.NONE;
    }




    private void createGpxOverlay() {
        int type = toType(infoCache.info);

        if (type == GpxType.WAY)
            gpxOverlay = new WayLayer(mcontext, color);

        else if (type == GpxType.RTE)
            gpxOverlay = new RouteLayer(mcontext, color);

        else
            gpxOverlay = new TrackLayer(mcontext);

    }


    private void createLegendOverlay() {
        int type = toType(infoCache.info);

        if (type == GpxType.WAY)
            legendOverlay = slegend.createWayLegendLayer();

        else if (type == GpxType.RTE)
            legendOverlay = slegend.createRouteLegendLayer();

        else
            legendOverlay = slegend.createTrackLegendLayer();
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }



    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
