package ch.bailu.aat_lib.map.layer.gpx;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList;
import ch.bailu.aat_lib.service.ServicesInterface;

public final class GpxOverlayListLayer implements MapLayerInterface {
    private final GpxDynLayer[] overlays;


    public GpxOverlayListLayer(StorageInterface s, MapContext mc, ServicesInterface services, DispatcherInterface d) {

        overlays = new GpxDynLayer[SolidOverlayFileList.MAX_OVERLAYS];

        for (int i = 0; i< overlays.length; i++) {
            overlays[i] = new GpxDynLayer(s, mc,services, d, InfoID.OVERLAY + i);
        }
    }

    @Override
    public void drawForeground(MapContext mcontext) {}

    @Override
    public boolean onTap(Point tapPos) {
        return false;
    }

    @Override
    public void drawInside(MapContext mcontext) {
        for (MapLayerInterface o : overlays) o.drawInside(mcontext);
    }

    @Override
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {
        for (MapLayerInterface o : overlays) o.onPreferencesChanged(s, key);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
