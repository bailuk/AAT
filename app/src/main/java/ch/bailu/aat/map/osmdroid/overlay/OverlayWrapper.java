package ch.bailu.aat.map.osmdroid.overlay;

import android.graphics.Canvas;
import android.view.MotionEvent;

import org.mapsforge.core.model.Point;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;

public class OverlayWrapper extends Overlay {
    public final MapLayerInterface overlay;
    public final MapContext mcontext;

    public OverlayWrapper(MapLayerInterface o, MapContext m) {
        overlay = o;
        mcontext = m;
    }

    @Override
    protected void draw(Canvas c, MapView osmv) {
        overlay.draw(mcontext);
    }

    @Override
    public boolean onSingleTapConfirmed(final MotionEvent e, final MapView mapView) {
        return overlay.onTap(null, null, new Point(e.getX(), e.getY()));
    }

}
