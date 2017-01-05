package ch.bailu.aat.map.mapsforge.context;

import android.content.Context;
import android.content.SharedPreferences;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.Layer;

import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.MapDraw;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapMetrics;
import ch.bailu.aat.map.TwoNodes;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.map.mapsforge.MapsForgeView;
import ch.bailu.aat.map.osmdroid.AndroidDraw;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeContext extends Layer implements MapContext, MapLayerInterface {

    private final ServiceContext scontext;
    private final Context context;

    private final String skey;

    private final AndroidDraw draw;
    private final MapsForgeMetrics metrics;
    private final TwoNodes nodes;

    private final MapsForgeView mapView;


    private boolean changed = true;



    public MapsForgeContext(MapsForgeView map, ServiceContext sc, String key) {
        metrics = new MapsForgeMetrics(sc, map);
        draw = new AndroidDraw(metrics.getDensity(), sc.getContext().getResources());
        nodes = new TwoNodes(metrics);
        skey = key;
        mapView = map;
        scontext = sc;
        context = sc.getContext();
    }



    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        this.changed = true;

    }

    @Override
    public void draw(MapContext mcontext) {

    }


    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {

        draw.init(canvas);
        metrics.init(boundingBox, canvas);


        if (changed) {
            changed = false;

            metrics.init(mapView, canvas);
            draw.init(metrics);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

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
        return scontext;
    }

    @Override
    public Context getContext() {
        return context;
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
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
