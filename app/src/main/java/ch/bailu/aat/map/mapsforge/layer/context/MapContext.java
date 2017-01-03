package ch.bailu.aat.map.mapsforge.layer.context;

import android.content.Context;
import android.content.SharedPreferences;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.mapsforge.MapsForgeView;
import ch.bailu.aat.map.mapsforge.layer.MapsForgeLayer;
import ch.bailu.aat.services.ServiceContext;

public class MapContext extends MapsForgeLayer {

    public final ServiceContext scontext;
    public final Context context;

    public final String skey;

    public final MapContextDraw draw;
    public final MapContextMetrics metrics;
    public final MapContextTwoNodes nodes;

    public final MapsForgeView mapView;


    private boolean changed = true;



    public MapContext(MapsForgeView map, ServiceContext sc, String key) {
        metrics = new MapContextMetrics(sc, map);
        draw = new MapContextDraw(metrics.density);
        nodes = new MapContextTwoNodes(metrics);
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
}
