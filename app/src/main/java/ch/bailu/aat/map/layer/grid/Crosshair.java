package ch.bailu.aat.map.layer.grid;

import android.content.SharedPreferences;

import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;

public class Crosshair implements MapLayerInterface {
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
        final android.graphics.Point pixel = mcontext.getMetrics().getCenterPixel();

        mcontext.draw().vLine(pixel.x);
        mcontext.draw().hLine(pixel.y);
        mcontext.draw().point(pixel);
    }



    @Override
    public boolean onTap(Point tapPos) {
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
