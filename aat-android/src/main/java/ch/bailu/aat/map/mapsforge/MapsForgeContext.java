package ch.bailu.aat.map.mapsforge;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.Layer;

import ch.bailu.aat.map.AndroidDraw;
import ch.bailu.aat.util.ui.AndroidAppDensity;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.MapDraw;
import ch.bailu.aat_lib.map.MapMetrics;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat_lib.map.MapsForgeMetrics;
import ch.bailu.aat_lib.map.TwoNodes;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class MapsForgeContext extends Layer implements MapContext, MapLayerInterface {

    private final String skey;

    private final AndroidDraw draw;
    private final MapsForgeMetrics metrics;
    private final TwoNodes nodes;

    private final MapsForgeViewBase mapView;


    public MapsForgeContext(MapsForgeViewBase map,
                            String key,
                            AndroidAppDensity d) {
        metrics = new MapsForgeMetrics(map, d);
        draw = new AndroidDraw(metrics.getDensity());
        nodes = new TwoNodes(metrics);
        skey = key;
        mapView = map;
    }


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    public void drawInside(MapContext mcontext) {}

    @Override
    public void drawForeground(MapContext mcontext) {}

    @Override
    public boolean onTap(ch.bailu.aat_lib.util.Point tapPos) {
        return false;
    }

    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        metrics.init(boundingBox, zoomLevel, canvas.getDimension(), topLeftPoint);
        draw.init(canvas, metrics);
    }


    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}

    @Override
    public MapMetrics getMetrics() {
        return metrics;
    }

    @Override
    public MapDraw draw() {
        return draw;
    }

    @Override
    public String getSolidKey() {
        return skey;
    }

    @Override
    public TwoNodes getTwoNodes() {
        return nodes;
    }

    @Override
    public MapViewInterface getMapView() {
        return mapView;
    }

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
