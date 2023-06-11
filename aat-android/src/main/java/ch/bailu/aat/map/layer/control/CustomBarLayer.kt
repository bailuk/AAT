package ch.bailu.aat.map.layer.control;

import ch.bailu.aat_lib.map.MapColor;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat_lib.map.edge.Position;


public class CustomBarLayer extends ControlBarLayer {
    public CustomBarLayer(MapContext mc, ControlBar b, UiTheme theme) {
        super(mc,b, Position.TOP, MapColor.setAlpha(theme.getBackgroundColor(), MapColor.ALPHA_LOW));
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
