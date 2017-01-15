package ch.bailu.aat.map.mapsforge;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.layer.MapPositionLayer;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeView extends MapsForgeViewBase {

    private final MapsForgeOnTopView overmap;
    private final MapsForgeTileLayerStack stack;

    public MapsForgeView(ServiceContext sc, DispatcherInterface dispatcher, String key) {
        super(sc, key, new MapDensity(sc.getContext()));

        MapPositionLayer pos = new MapPositionLayer(getMContext(), dispatcher);
        add(pos);

        stack = new MapsForgeTileLayerStack(this);
        add(stack);

        setClickable(true);


        overmap = new MapsForgeOnTopView(this,
                getMContext(),
                new MapDensity(sc.getContext()),
                getLayers());

        addView(overmap);
    }

    @Override
    public void reDownloadTiles() {
        stack.reDownloadTiles();
    }

    @Override
    public void onLayout(boolean c, int l, int t, int r, int b) {
        overmap.layout(0,0,r-l, b-t);

        super.onLayout(c,l,t,r,b);
    }
}
