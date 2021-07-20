package ch.bailu.aat.map.mapsforge;

import android.content.Context;
import android.graphics.Canvas;

import org.mapsforge.core.model.Dimension;
import org.mapsforge.map.android.view.MapView;

import java.util.ArrayList;

import ch.bailu.aat.map.AndroidDraw;
import ch.bailu.aat.map.AndroidMapContext;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat.map.To;
import ch.bailu.aat_lib.map.MapsForgeMetrics;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AndroidAppDensity;
import ch.bailu.aat_lib.map.MapDraw;
import ch.bailu.aat_lib.map.MapMetrics;
import ch.bailu.aat_lib.map.TwoNodes;

public class MapsForgeForeground implements AndroidMapContext {

    private final MapContext mcontext;
    private final AndroidDraw draw;
    private final MapsForgeMetrics metrics;

    private final ArrayList<MapLayerInterface> layers;

    public MapsForgeForeground(MapView mapView, MapContext mc, AndroidAppDensity d, ArrayList<MapLayerInterface> l) {
        mcontext = mc;
        layers = l;

        metrics = new MapsForgeMetrics(mapView, d);
        draw = new AndroidDraw(mc.getMetrics().getDensity());

    }


    public void dispatchDraw(final Canvas canvas) {

        new InsideContext(To.scontext(mcontext)) {
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
        return To.scontext(mcontext);
    }

    @Override
    public Context getContext() {
        return To.context(mcontext);
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
