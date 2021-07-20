package ch.bailu.aat.map.mapsforge;

import android.content.Context;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.Layer;

import ch.bailu.aat.app.App;
import ch.bailu.aat.map.AndroidDraw;
import ch.bailu.aat.map.AndroidMapContext;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.MapDraw;
import ch.bailu.aat_lib.map.MapMetrics;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat_lib.map.MapsForgeMetrics;
import ch.bailu.aat_lib.map.TwoNodes;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AndroidAppDensity;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class MapsForgeContext extends Layer implements AndroidMapContext, MapLayerInterface {

    private final ServiceContext scontext;
    private final Context context;

    private final String skey;

    private final AndroidDraw draw;
    private final MapsForgeMetrics metrics;
    private final TwoNodes nodes;

    private final MapsForgeViewBase mapView;


    public MapsForgeContext(MapsForgeViewBase map,
                            ServiceContext sc,
                            String key,
                            AndroidAppDensity d) {
        metrics = new MapsForgeMetrics(map, d);
        draw = new AndroidDraw(metrics.getDensity());
        nodes = new TwoNodes(metrics);
        skey = key;
        mapView = map;
        scontext = sc;
        context = sc.getContext();
    }


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void drawInside(MapContext mcontext) {

    }

    @Override
    public void drawForeground(MapContext mcontext) {

    }

    @Override
    public boolean onTap(Point tapPos) {
        return false;
    }


    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        metrics.init(boundingBox, zoomLevel, canvas, topLeftPoint);
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


    public static void initMapsForge(App app) {
        AndroidGraphicFactory.createInstance(app);

        Parameters.SQUARE_FRAME_BUFFER = false; // move to app
    }
}
