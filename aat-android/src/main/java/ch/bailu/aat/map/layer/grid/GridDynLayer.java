package ch.bailu.aat.map.layer.grid;


import ch.bailu.aat.preferences.map.SolidMapGrid;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class GridDynLayer implements MapLayerInterface {
    private MapLayerInterface gridLayer;
    private final SolidMapGrid sgrid;

    private final MapContext mcontext;


    public GridDynLayer(StorageInterface s, MapContext mc) {
        mcontext = mc;
        sgrid = new SolidMapGrid(s, mc.getSolidKey());
        gridLayer = sgrid.createGridLayer();
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
            gridLayer = sgrid.createGridLayer();
            mcontext.getMapView().requestRedraw();
        }

    }

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
