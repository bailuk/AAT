package ch.bailu.aat.views.map.overlay.gpx;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat.preferences.SolidLegend;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.NullOverlay;
import ch.bailu.aat.views.map.overlay.OsmOverlay;

public class GpxDynOverlay extends OsmOverlay {

    private OsmOverlay gpx;
    private OsmOverlay legend;
    
    private final int ID;
    private final int color;

    private final CacheService cache;
    
    private final SolidLegend slegend;
    
    private GpxInformation info;
    
    
    public GpxDynOverlay(AbsOsmView map, CacheService cs, int id) {
        this(map,cs, id,-1);
    }

    public GpxDynOverlay(AbsOsmView map, CacheService cs,  int id, int c) {
        super(map);
        color=c;
        cache = cs;
        ID = id;
        gpx = new NullOverlay(map);
        legend = new NullOverlay(map);
        
        slegend = new SolidLegend(map.getContext(), map.solidKey);
    }




    @Override
    public void draw(MapPainter p) {
        gpx.draw(p);
        legend.draw(p);
    }


    @Override
    public void updateGpxContent(GpxInformation i) {
        if (i.getID()== ID) {
            info=i;
            setTrack(info.getGpxList());
            gpx.updateGpxContent(info);
            legend.updateGpxContent(info);
        }

    }



    public void setTrack(GpxList gpxList) {

        if (gpxList.getDelta().getType()==GpxBigDeltaInterface.WAY) {
            if (color == -1) gpx = new WayOverlay(getOsmView(), cache, ID);
            else gpx = new WayOverlay(getOsmView(), cache, ID, color);
            legend = slegend.createWayLegendOverlay(getOsmView(), ID);

        } else if (gpxList.getDelta().getType()==GpxBigDeltaInterface.RTE) {
            if (color == -1)  gpx = new RouteOverlay(getOsmView(), ID);
            else gpx = new RouteOverlay(getOsmView(), ID, color);
            legend = slegend.createRouteLegendOverlay(getOsmView(), ID);
                
        } else {
            gpx = new TrackOverlay(getOsmView(), ID);
            legend = slegend.createTrackLegendOverlay(getOsmView(), ID);
        }
    }

    
    @Override
    public void onSharedPreferenceChanged(String key) {
        if (slegend.hasKey(key) && info != null) updateGpxContent(info);
    }
}
