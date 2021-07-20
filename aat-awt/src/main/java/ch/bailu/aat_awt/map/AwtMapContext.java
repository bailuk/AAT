package ch.bailu.aat_awt.map;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.Layer;

import ch.bailu.aat_lib.map.AppDensity;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.MapDraw;
import ch.bailu.aat_lib.map.MapMetrics;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat_lib.map.MapsForgeMetrics;
import ch.bailu.aat_lib.map.TwoNodes;

public class AwtMapContext extends Layer implements MapContext {

    private final MapViewInterface mapView;
    private final MapsForgeMetrics metrics;
    private final String key;
    private final AwtMapDraw draw = new AwtMapDraw();
    private final TwoNodes twoNodes;



    public AwtMapContext(AwtCustomMapView v, String k) {
        metrics = new MapsForgeMetrics(v, new AppDensity());
        key = k;
        mapView = v;
        twoNodes = new TwoNodes(metrics);
    }


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
        return key;
    }

    @Override
    public TwoNodes getTwoNodes() {
        return twoNodes;
    }

    @Override
    public MapViewInterface getMapView() {
        return mapView;
    }

    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        metrics.init(boundingBox, zoomLevel, canvas, topLeftPoint);
        draw.init(canvas, metrics);
    }
}
