package ch.bailu.aat.views.map.overlay.gpx;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.OsmOverlay;


public class GpxOverlayListOverlay extends OsmOverlay {
    private final GpxDynOverlay[] overlays;


    public GpxOverlayListOverlay(OsmInteractiveView v, DispatcherInterface d, ServiceContext sc) {
        super(v);
        
        overlays = new GpxDynOverlay[OverlaySource.MAX_OVERLAYS];

        for (int i = 0; i< overlays.length; i++) {
            overlays[i] = new GpxDynOverlay(v, sc, AppTheme.OVERLAY_COLOR[i + 4]);
            d.addTarget(overlays[i], InfoID.OVERLAY + i);
        }
    }

    @Override
    public void draw(MapPainter p) {
        for (OsmOverlay o : overlays) o.draw(p);
    }




    @Override
    public void onSharedPreferenceChanged(String key) {
        for (OsmOverlay o : overlays) o.onSharedPreferenceChanged(key);
    }
}
