package ch.bailu.aat_lib.map.layer.grid;


import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidMapGrid;
import ch.bailu.aat_lib.service.ServicesInterface;

public final class GridDynLayer implements MapLayerInterface {
    private MapLayerInterface gridLayer;
    private final SolidMapGrid sgrid;

    private final MapContext mcontext;
    private final ServicesInterface services;

    public GridDynLayer(ServicesInterface services, StorageInterface s, MapContext mc) {
        mcontext = mc;
        this.services = services;
        sgrid = new SolidMapGrid(s, mc.getSolidKey());
        gridLayer = sgrid.createGridLayer(services);
    }


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    public void drawInside(MapContext mcontext) {
        gridLayer.drawInside(mcontext);
    }

    @Override
    public boolean onTap(Point tapXY) {
        return false;
    }

    @Override
    public void drawForeground(MapContext mcontext) {
        gridLayer.drawForeground(mcontext);
    }


    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {
        if (sgrid.hasKey(key)) {
            gridLayer = sgrid.createGridLayer(services);
            mcontext.getMapView().requestRedraw();
        }

    }

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
