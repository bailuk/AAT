package ch.bailu.aat.mapsforge.layer.grid;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.mapsforge.layer.ContextLayer;
import ch.bailu.aat.mapsforge.layer.MapsForgeLayer;
import ch.bailu.aat.preferences.SolidMapGrid;

public class GridDynLayer extends MapsForgeLayer {
    private MapsForgeLayer gridLayer;
    private final SolidMapGrid sgrid;

    private final ContextLayer clayer;


    public GridDynLayer(ContextLayer cl) {
        clayer = cl;
        sgrid = new SolidMapGrid(cl.getContext(), cl.getMapView().getSolidKey());
        gridLayer = sgrid.createGridOverlay(cl);
    }




    @Override
    public void onSharedPreferenceChanged(String key) {
        if (sgrid.hasKey(key)) {
            gridLayer = sgrid.createGridOverlay(clayer);
            clayer.getMapView().refreshDrawableState();
        }
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {

    }

    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        gridLayer.draw(boundingBox, zoomLevel, canvas, topLeftPoint);
    }
}
