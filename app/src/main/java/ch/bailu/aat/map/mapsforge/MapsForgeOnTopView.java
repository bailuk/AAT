package ch.bailu.aat.map.mapsforge;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Looper;
import android.view.View;

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
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeOnTopView extends View implements MapContext {


    private final MapContext mcontext;
    private final AndroidDraw draw;
    private final MapsForgeMetrics metrics;

    private final ArrayList<MapLayerInterface> layers;

    public MapsForgeOnTopView(MapView mapView, MapContext mc, ArrayList<MapLayerInterface> l) {
        super(mc.getContext());

        setWillNotDraw(false);

        mcontext = mc;
        layers = l;

        metrics = new MapsForgeMetrics(mc.getSContext(), mapView);
        draw = new AndroidDraw(mc.getMetrics().getDensity(), mc.getContext().getResources());
    }

    boolean attached=false;

    @Override
    public void onAttachedToWindow() {
        attached = true;
    }

    public void repaint() {
        if (attached)
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                invalidate();
            } else {
                postInvalidate();
            }
    }


    @Override
    public void onDraw(Canvas canvas) {
        metrics.init(new Dimension(canvas.getWidth(), canvas.getHeight()));
        draw.init(canvas, metrics);

        canvas.drawColor(Color.TRANSPARENT);
        for (MapLayerInterface l: layers) l.drawOnTop(this);
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
