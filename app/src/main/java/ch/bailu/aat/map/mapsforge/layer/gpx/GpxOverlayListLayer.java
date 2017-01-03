package ch.bailu.aat.map.mapsforge.layer.gpx;

import android.content.SharedPreferences;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.map.mapsforge.layer.MapsForgeLayer;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;

public class GpxOverlayListLayer extends MapsForgeLayer {
    private final GpxDynLayer[] overlays;


    public GpxOverlayListLayer(MapContext mc, DispatcherInterface d) {

        overlays = new GpxDynLayer[OverlaySource.MAX_OVERLAYS];

        for (int i = 0; i< overlays.length; i++) {
            overlays[i] = new GpxDynLayer(mc, AppTheme.OVERLAY_COLOR[i + 4]);
            d.addTarget(overlays[i], InfoID.OVERLAY + i);
        }
    }

    @Override
    public void draw(BoundingBox b, byte z, Canvas c, Point t) {
        for (MapsForgeLayer o : overlays) o.draw(b,z,c,t);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        for (MapsForgeLayer o : overlays) o.onSharedPreferenceChanged(sharedPreferences, key);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

}
