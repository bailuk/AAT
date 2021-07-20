package ch.bailu.aat.map.layer.gpx;

import org.mapsforge.core.model.Point;

import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat.preferences.map.SolidOverlayFileList;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class GpxOverlayListLayer implements MapLayerInterface {
    private final GpxDynLayer[] overlays;


    public GpxOverlayListLayer(MapContext mc, DispatcherInterface d) {

        overlays = new GpxDynLayer[SolidOverlayFileList.MAX_OVERLAYS];

        for (int i = 0; i< overlays.length; i++) {
            overlays[i] = new GpxDynLayer(mc, d, InfoID.OVERLAY +i);
        }
    }

    @Override
    public void drawForeground(MapContext mcontext) {}

    @Override
    public void drawInside(MapContext mcontext) {
        for (MapLayerInterface o : overlays) o.drawInside(mcontext);
    }

    @Override
    public boolean onTap(Point tapXY) {
        return false;
    }


    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {
        for (MapLayerInterface o : overlays) o.onPreferencesChanged(s, key);
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
