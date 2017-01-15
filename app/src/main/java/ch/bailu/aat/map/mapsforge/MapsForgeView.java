package ch.bailu.aat.map.mapsforge;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.model.common.Observer;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.layer.MapPositionLayer;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeView extends MapsForgeViewBase {

    private final MapsForgeOnTopView overmap;
    private final MapsForgeTileLayerStack stack;
    private final MapPositionLayer pos;

    public MapsForgeView(ServiceContext sc, DispatcherInterface dispatcher, String key) {
        super(sc, key, new MapDensity(sc.getContext()));

        pos = new MapPositionLayer(getMContext(), dispatcher);
        add(pos);

        stack = new MapsForgeTileLayerStack(this);
        add(stack);

        overmap = new MapsForgeOnTopView(this,
                getMContext(),
                new MapDensity(sc.getContext()),
                getLayers());
        addView(overmap);

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
    public void onLayout(boolean c, int l, int t, int r, int b) {
        overmap.layout(0, 0, r - l, b - t);
        super.onLayout(c, l, t, r, b);
    }


    @Override
    public void repaint() {
        if (overmap != null) overmap.repaint();
        super.repaint();
    }

}
