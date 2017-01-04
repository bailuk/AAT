package ch.bailu.aat.map.layer.gpx;

import android.content.SharedPreferences;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.util.ui.AppTheme;

public class GpxOverlayListLayer implements MapLayerInterface {
    private final GpxDynLayer[] overlays;


    public GpxOverlayListLayer(MapContext mc, DispatcherInterface d) {

        overlays = new GpxDynLayer[OverlaySource.MAX_OVERLAYS];

        for (int i = 0; i< overlays.length; i++) {
            overlays[i] = new GpxDynLayer(mc, AppTheme.OVERLAY_COLOR[i + 4]);
            d.addTarget(overlays[i], InfoID.OVERLAY + i);
        }
    }

    @Override
    public void draw(MapContext mcontext) {
        for (MapLayerInterface o : overlays) o.draw(mcontext);
    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        return false;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        for (MapLayerInterface o : overlays) o.onSharedPreferenceChanged(sharedPreferences, key);
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
