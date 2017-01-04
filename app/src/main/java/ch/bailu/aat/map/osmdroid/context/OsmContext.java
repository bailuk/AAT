package ch.bailu.aat.map.osmdroid.context;

import android.content.Context;
import android.graphics.Canvas;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapDraw;
import ch.bailu.aat.map.MapMetrics;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.TwoNodes;
import ch.bailu.aat.map.osmdroid.NewOsmView;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppDensity;

public class OsmContext extends Overlay implements MapContext  {
    private final OsmMetrics metrics;
    private final TwoNodes nodes;
    private final OsmDraw draw;

    private final ServiceContext scontext;

    private final MapViewInterface mapView;
    private final String skey;

    public OsmContext(NewOsmView v, ServiceContext sc, AppDensity res, String s) {
        scontext = sc;
        metrics =new OsmMetrics(res, v.map);
        nodes=new TwoNodes(metrics);
        draw =new OsmDraw(metrics, this);
        mapView = v;
        skey=s;
    }

    public void init(Canvas c, MapView map) {
        metrics.init(map);
        draw.init(c);
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
        return scontext.getContext();
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
    protected void draw(Canvas c, MapView osmv) {
        init(c, osmv);
    }
}
