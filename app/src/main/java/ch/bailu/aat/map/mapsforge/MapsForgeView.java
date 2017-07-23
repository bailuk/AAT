package ch.bailu.aat.map.mapsforge;

import android.graphics.Canvas;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.model.common.Observer;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.layer.MapPositionLayer;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeView extends MapsForgeViewBase {

    private final MapsForgeForeground foreground;
    private final MapsForgeTileLayerStackConfigured stack;
    private final MapPositionLayer pos;

    public MapsForgeView(ServiceContext sc, DispatcherInterface dispatcher, String key) {
        super(sc, key, new MapDensity(sc.getContext()));

        pos = new MapPositionLayer(getMContext(), dispatcher);
        add(pos);

        stack = new MapsForgeTileLayerStackConfigured(this);
        add(stack, stack);

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

                if (newCenter.equals(center) == false) {
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
