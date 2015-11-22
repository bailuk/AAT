package ch.bailu.aat.views.map.overlay;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import android.content.Context;
import android.graphics.Canvas;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.views.map.AbsOsmView;

public abstract class OsmOverlay extends Overlay implements DescriptionInterface {
    private final AbsOsmView osm;
    private final MapPainter painter;

    
    public OsmOverlay(AbsOsmView absOsmView) {
        super(absOsmView.getContext());
        painter = new MapPainter(absOsmView.getContext());
        osm = absOsmView;
    }


    @Override
    public void draw(Canvas c, MapView m, boolean shadow) {
        if (!shadow && !m.isAnimating()) {
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
    public void updateGpxContent(GpxInformation info) {}

    public void onSharedPreferenceChanged(String key) {
    }

}
