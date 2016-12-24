package ch.bailu.aat.views.map.overlay.gpx;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxInformationCache;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.preferences.SolidLegend;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.OsmOverlay;

public class GpxDynOverlay extends OsmOverlay implements OnContentUpdatedInterface {

    private final GpxInformationCache infoCache = new GpxInformationCache();

    private GpxOverlay gpxOverlay;
    private GpxOverlay legendOverlay;

    private final SolidLegend slegend;

    private final ServiceContext scontext;
    private final int color;



    public GpxDynOverlay(AbsOsmView map, ServiceContext sc, int c) {
        super(map);

        scontext = sc;
        color = c;

        slegend = new SolidLegend(map.getContext(), map.getSolidKey());

        createLegendOverlay();
        createGpxOverlay();
    }


    public GpxDynOverlay(AbsOsmView map, ServiceContext sc,
                         DispatcherInterface dispatcher, int iid) {
        this(map, sc, -1);
        dispatcher.addTarget(this, iid);
    }


    @Override
    public void draw(MapPainter p) {
        gpxOverlay.draw(p);
        legendOverlay.draw(p);
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

        getOsmView().requestRedraw();
    }



    @Override
    public void onSharedPreferenceChanged(String key) {
        if (slegend.hasKey(key)) {
            createLegendOverlay();
            infoCache.letUpdate(legendOverlay);
            getOsmView().requestRedraw();
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
            gpxOverlay = new WayOverlay(getOsmView(), scontext, color);

        else if (type == GpxType.RTE)
            gpxOverlay = new RouteOverlay(getOsmView(), color);

        else
            gpxOverlay = new TrackOverlay(getOsmView());

    }


    private void createLegendOverlay() {
        int type = toType(infoCache.info);

        if (type == GpxType.WAY)
            legendOverlay = slegend.createWayLegendOverlay(getOsmView());

        else if (type == GpxType.RTE)
            legendOverlay = slegend.createRouteLegendOverlay(getOsmView());

        else
            legendOverlay = slegend.createTrackLegendOverlay(getOsmView());
    }
}
