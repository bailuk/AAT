package ch.bailu.aat.map.layer.grid;

import org.mapsforge.core.model.Point;

import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class Crosshair implements MapLayerInterface {
    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void drawInside(MapContext mcontext) {

    }

    @Override
    public void drawForeground(MapContext mcontext) {
        drawGrid(mcontext);



    }


    private void drawGrid(MapContext mcontext) {
        final ch.bailu.aat_lib.map.Point pixel = mcontext.getMetrics().getCenterPixel();

        mcontext.draw().vLine(pixel.x);
        mcontext.draw().hLine(pixel.y);
        mcontext.draw().point(pixel);
    }



    @Override
    public boolean onTap(Point tapPos) {
        return false;
    }

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}


    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
