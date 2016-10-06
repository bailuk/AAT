package ch.bailu.aat.views.map.overlay.control;

import android.graphics.Color;

import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.map.OsmInteractiveView;


public class CustomBarOverlay extends ControlBarOverlay {
    private final static int TRANSPARENT = Color.argb(200, 0, 0, 0);


    public CustomBarOverlay(OsmInteractiveView v, ControlBar b) {
        super(v,b, TRANSPARENT);
        showBar();

    }

    public CustomBarOverlay(OsmInteractiveView v) {
        super(v, 
                new ControlBar(
                        v.getContext(),
                        AppLayout.getOrientationAlongSmallSide(v.getContext())),
                Color.BLACK);
    }


    @Override
    public void showBar() {
        showBarAtTop();
    }


    @Override
    public void topTap() {
        showBar();
    }



}
