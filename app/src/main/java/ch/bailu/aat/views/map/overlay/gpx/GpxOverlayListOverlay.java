package ch.bailu.aat.views.map.overlay.gpx;

import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.OsmOverlay;


public class GpxOverlayListOverlay extends OsmOverlay {
    private final OsmOverlay[] list;


    public GpxOverlayListOverlay(OsmInteractiveView v, ServiceContext sc) {
        super(v);
        
        list = new GpxDynOverlay[OverlaySource.MAX_OVERLAYS];

        for (int i=0; i<list.length; i++) 
            list[i] = new GpxDynOverlay(v, sc, GpxInformation.ID.INFO_ID_OVERLAY+i, 
                    AppTheme.OVERLAY_COLOR[i+4]);
    }

    @Override
    public void draw(MapPainter p) {
        for (int i=0; i<list.length; i++)
            list[i].draw(p);
    }


    @Override
    public void updateGpxContent(GpxInformation info) {
        for (int i=0; i<list.length; i++)
            list[i].updateGpxContent(info);
    }


    @Override
    public void onSharedPreferenceChanged(String key) {
        for (int i=0; i<list.length; i++)
            list[i].onSharedPreferenceChanged(key);
    }
}
