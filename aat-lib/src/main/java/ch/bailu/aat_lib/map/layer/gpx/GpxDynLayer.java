package ch.bailu.aat_lib.map.layer.gpx;

import ch.bailu.aat_lib.preferences.map.SolidLegend;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxInformationCache;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public final class GpxDynLayer implements MapLayerInterface, OnContentUpdatedInterface {
    private final GpxInformationCache infoCache = new GpxInformationCache();

    private GpxLayer gpxOverlay;
    private GpxLayer legendOverlay;

    private final SolidLegend slegend;

    private final MapContext mcontext;

    private final ServicesInterface services;

    public GpxDynLayer(StorageInterface s, MapContext mc, ServicesInterface services) {
        mcontext = mc;
        this.services = services;
        slegend = new SolidLegend(s, mcontext.getSolidKey());

        createLegendOverlay();
        createGpxOverlay();
    }

    public GpxDynLayer(StorageInterface s, MapContext mc, ServicesInterface services,
                         DispatcherInterface dispatcher, int iid) {
        this(s, mc, services);
        dispatcher.addTarget(this, iid);
    }

    @Override
    public void drawInside(MapContext mcontext) {
        gpxOverlay.drawInside(mcontext);
        legendOverlay.drawInside(mcontext);
    }

    @Override
    public void drawForeground(MapContext mcontext) {}

    @Override
    public boolean onTap(Point tapPos) {
        return false;
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
    public void onPreferencesChanged(StorageInterface s, String key) {

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
        gpxOverlay = Factory.get(type).layer(mcontext, services,0);
    }


    private void createLegendOverlay() {
        GpxType type = toType(infoCache.info);
        legendOverlay = Factory.get(type).legend(slegend.getStorage(), slegend, 0);
    }


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
