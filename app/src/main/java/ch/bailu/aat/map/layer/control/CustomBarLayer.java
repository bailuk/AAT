package ch.bailu.aat.map.layer.control;

import android.graphics.Color;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.views.bar.ControlBar;


public class CustomBarLayer extends ControlBarLayer {
    private final static int TRANSPARENT = Color.argb(200, 0, 0, 0);


    public CustomBarLayer(MapContext mc, ControlBar b) {
        super(mc,b, TOP, TRANSPARENT);
        showBar();
    }

    @Override
    public void drawInside(MapContext mcontext) {

    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
