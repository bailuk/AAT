package ch.bailu.aat_awt.map;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.map.awt.view.MapView;

import java.util.ArrayList;

import ch.bailu.aat_lib.map.AppDensity;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.MapDraw;
import ch.bailu.aat_lib.map.MapMetrics;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat_lib.map.MapsForgeMetrics;
import ch.bailu.aat_lib.map.TwoNodes;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;

public class AwtMapContextForeground implements MapContext {

    private final MapContext mcontext;
    private final MapsForgeMetrics metrics;

    private final AwtMapDraw draw;

    private final ArrayList<MapLayerInterface> layers;

    public AwtMapContextForeground(MapView mapView, MapContext mc, AppDensity d, ArrayList<MapLayerInterface> l) {
        mcontext = mc;
        layers = l;

        metrics = new MapsForgeMetrics(mapView, d);
        draw = new AwtMapDraw();

    }


    public void dispatchDraw(final Canvas canvas) {
        metrics.init(new Dimension(canvas.getWidth(), canvas.getHeight()));
        draw.init(canvas, metrics);

        for (MapLayerInterface l : layers) {
            l.drawForeground(this);
        }
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
        return mcontext.getSolidKey();
    }

    @Override
    public TwoNodes getTwoNodes() {
        return mcontext.getTwoNodes();
    }

    @Override
    public MapViewInterface getMapView() {
        return mcontext.getMapView();
    }
}
