package ch.bailu.aat.map.mapsforge;

import android.graphics.Canvas;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.model.common.Observer;

import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat_lib.map.layer.MapPositionLayer;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;

public class MapsForgeView extends MapsForgeViewBase {

    private final MapsForgeForeground foreground;
    private final MapsForgeTileLayerStackConfigured stack;
    private final MapPositionLayer pos;

    public MapsForgeView(ServiceContext sc, DispatcherInterface dispatcher, String key) {
        super(sc, key, new MapDensity(sc.getContext()));

        stack = new MapsForgeTileLayerStackConfigured.All(this);
        add(stack, stack);

        // Depends on zoom limits (setItem by TileLayerStack)
        pos = new MapPositionLayer(getMContext(), new Storage(sc.getContext()), dispatcher);
        add(pos);

        foreground = new MapsForgeForeground(this,
                getMContext(),
                new MapDensity(sc.getContext()),
                getLayers());

        setClickable(true);

        getModel().mapViewPosition.addObserver(new Observer() {
            private LatLong center = getModel().mapViewPosition.getCenter();

            @Override
            public void onChange() {
                LatLong newCenter = getModel().mapViewPosition.getCenter();

                if (newCenter != null && newCenter.equals(center) == false) {
                    center = newCenter;
                    pos.onMapCenterChanged(center);
                }
            }
        });

    }


    @Override
    public void reDownloadTiles() {
        stack.reDownloadTiles();
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        foreground.dispatchDraw(canvas);
    }
}
