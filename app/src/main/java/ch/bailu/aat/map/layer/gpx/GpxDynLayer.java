package ch.bailu.aat.map.layer.gpx;

import android.content.SharedPreferences;

import org.mapsforge.core.model.Point;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxInformationCache;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.preferences.map.SolidLegend;

public class GpxDynLayer implements MapLayerInterface, OnContentUpdatedInterface {
    private final GpxInformationCache infoCache = new GpxInformationCache();

    private GpxLayer gpxOverlay;
    private GpxLayer legendOverlay;

    private final SolidLegend slegend;

    private final MapContext mcontext;


    public GpxDynLayer(MapContext mc) {
        mcontext = mc;
        slegend = new SolidLegend(mcontext.getContext(), mcontext.getSolidKey());

        createLegendOverlay();
        createGpxOverlay();
    }


    public GpxDynLayer(MapContext mc,
                         DispatcherInterface dispatcher, int iid) {
        this(mc);
        dispatcher.addTarget(this, iid);
    }



    @Override
    public void drawInside(MapContext mcontext) {
        gpxOverlay.drawInside(mcontext);
        legendOverlay.drawInside(mcontext);
    }

    @Override
    public boolean onTap(Point tapXY) {
        return false;
    }

    @Override
    public void drawForeground(MapContext mcontext) {

    }

    private GpxType type = GpxType.NONE;

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        infoCache.set(iid, info);


        if (type != toType(info)) {
            type = toType(info);
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

    private static GpxType toType(GpxInformation i) {
        if (i != null && i.getGpxList() != null) {
            return i.getGpxList().getDelta().getType();
        }
        return GpxType.NONE;
    }




    private void createGpxOverlay() {
        GpxType type = toType(infoCache.info);

        gpxOverlay = Factory.get(type).layer(mcontext, 0);
    }


    private void createLegendOverlay() {
        GpxType type = toType(infoCache.info);

        legendOverlay = Factory.get(type).legend(slegend, 0);
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
