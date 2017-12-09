package ch.bailu.aat.map.mapsforge;

import android.content.Context;
import android.graphics.Canvas;

import org.mapsforge.core.model.Dimension;
import org.mapsforge.map.android.view.MapView;

import java.util.ArrayList;

import ch.bailu.aat.map.AndroidDraw;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapDraw;
import ch.bailu.aat.map.MapMetrics;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.TwoNodes;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppDensity;

public class MapsForgeForeground implements MapContext {

    private final MapContext mcontext;
    private final AndroidDraw draw;
    private final MapsForgeMetrics metrics;

    private final ArrayList<MapLayerInterface> layers;

    public MapsForgeForeground(MapView mapView, MapContext mc, AppDensity d, ArrayList<MapLayerInterface> l) {
        mcontext = mc;
        layers = l;

        metrics = new MapsForgeMetrics(mapView, d);
        draw = new AndroidDraw(mc.getMetrics().getDensity(), mc.getContext().getResources());

    }


    public void dispatchDraw(final Canvas canvas) {

        new InsideContext(mcontext.getSContext()) {
            @Override
            public void run() {
                metrics.init(new Dimension(canvas.getWidth(), canvas.getHeight()));
                draw.init(canvas, metrics);

                for (MapLayerInterface l : layers) {
                    l.drawForeground(MapsForgeForeground.this);
                }
            }
        };
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
    public ServiceContext getSContext() {
        return mcontext.getSContext();
    }

    @Override
    public Context getContext() {
        return mcontext.getContext();
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
