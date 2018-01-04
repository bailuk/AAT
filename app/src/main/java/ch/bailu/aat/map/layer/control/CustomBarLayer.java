package ch.bailu.aat.map.layer.control;

import ch.bailu.aat.map.MapColor;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.views.bar.ControlBar;


public class CustomBarLayer extends ControlBarLayer {



    public CustomBarLayer(MapContext mc, ControlBar b) {
        super(mc,b, TOP, MapColor.DARK);
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
