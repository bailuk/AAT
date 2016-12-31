package ch.bailu.aat.mapsforge.layer.grid;

import android.content.SharedPreferences;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.mapsforge.layer.context.MapContext;
import ch.bailu.aat.mapsforge.layer.MapsForgeLayer;
import ch.bailu.aat.preferences.SolidMapGrid;

public class GridDynLayer extends MapsForgeLayer {
    private MapsForgeLayer gridLayer;
    private final SolidMapGrid sgrid;

    private final MapContext clayer;


    public GridDynLayer(MapContext cl) {
        clayer = cl;
        sgrid = new SolidMapGrid(cl.context, cl.skey);
        gridLayer = sgrid.createGridOverlay(cl);
    }





    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        gridLayer.draw(boundingBox, zoomLevel, canvas, topLeftPoint);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (sgrid.hasKey(key)) {
            gridLayer = sgrid.createGridOverlay(clayer);
            requestRedraw();
        }

    }
}
