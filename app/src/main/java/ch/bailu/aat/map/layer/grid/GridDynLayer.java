package ch.bailu.aat.map.layer.grid;

import android.content.SharedPreferences;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.preferences.SolidMapGrid;

public class GridDynLayer implements MapLayerInterface {
    private MapLayerInterface gridLayer;
    private final SolidMapGrid sgrid;

    private final MapContext mcontext;


    public GridDynLayer(MapContext cl) {
        mcontext = cl;
        sgrid = new SolidMapGrid(cl.getContext(), cl.getSolidKey());
        gridLayer = sgrid.createGridOverlay(cl);
    }





    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void draw(MapContext mcontext) {
        gridLayer.draw(mcontext);
    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        return false;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (sgrid.hasKey(key)) {
            gridLayer = sgrid.createGridOverlay(mcontext);
            mcontext.getMapView().requestRedraw();
        }

    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
