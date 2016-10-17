package ch.bailu.aat.views.map.overlay;

import android.content.Context;
import android.graphics.Canvas;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import ch.bailu.aat.description.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.views.map.AbsOsmView;

public abstract class OsmOverlay extends Overlay implements OnContentUpdatedInterface {
    private final AbsOsmView osm;
    private final MapPainter painter;

    
    public OsmOverlay(AbsOsmView absOsmView) {
        painter = new MapPainter(absOsmView.getContext());
        osm = absOsmView;
    }


    @Override
    public void draw(Canvas c, MapView m) {
        if (!m.isAnimating()) {
            osm.mapIconCache.newPass();
            painter.init(c,m);
            draw(painter);
        }
    }

    public abstract void draw(MapPainter p);


    public AbsOsmView getOsmView() {
        return osm;
    }

    public MapView getMapView() {
        return osm.map;
    }

    public Context getContext() {
        return osm.getContext();
    }

    @Override
    public void onContentUpdated(GpxInformation info) {}

    public void onSharedPreferenceChanged(String key) {
    }

    public void onLayout(boolean changed, int l, int t, int r, int b) {}
}
