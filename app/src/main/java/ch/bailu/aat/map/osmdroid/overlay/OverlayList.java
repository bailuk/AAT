package ch.bailu.aat.map.osmdroid.overlay;

import android.view.MotionEvent;

import org.osmdroid.views.MapView;

import java.util.ArrayList;

import ch.bailu.aat.map.osmdroid.AbsOsmView;

public class OverlayList extends OsmOverlay {
    private final ArrayList<OsmOverlay> overlays = new ArrayList(10);

    public OverlayList(AbsOsmView absOsmView) {
        super(absOsmView);
    }



    public OsmOverlay add(OsmOverlay o) {
        overlays.add(o);
        return o;
    }



    @Override
    public boolean onSingleTapConfirmed(MotionEvent e, MapView map) {
        for (OsmOverlay overlay: overlays)
            overlay.onSingleTapConfirmed(e, map);

        return false;
    }


    @Override
    public void draw(MapPainter painter) {
        for (OsmOverlay overlay: overlays)
            overlay.draw(painter);
    }


    @Override
    public void onSharedPreferenceChanged(String key) {
        for (OsmOverlay overlay: overlays)
            overlay.onSharedPreferenceChanged(key);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        for (OsmOverlay overlay: overlays)
            overlay.onLayout(changed, l, t, r, b);
    }
}
