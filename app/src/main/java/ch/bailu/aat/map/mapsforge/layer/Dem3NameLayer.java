package ch.bailu.aat.map.mapsforge.layer;

import android.content.SharedPreferences;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;

public class Dem3NameLayer extends MapsForgeLayer{

    private final MapContext mcontext;

    public Dem3NameLayer(MapContext mc) {
        mcontext = mc;
    }


    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {

        final SrtmCoordinates c = new SrtmCoordinates(boundingBox.getCenterPoint());
        mcontext.draw.textBottom(c.toString(),4);
    }



    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

}
