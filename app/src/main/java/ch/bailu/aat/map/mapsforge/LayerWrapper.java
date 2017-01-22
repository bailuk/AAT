package ch.bailu.aat.map.mapsforge;

import android.content.Context;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;

import ch.bailu.aat.map.AndroidDraw;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapDraw;
import ch.bailu.aat.map.MapMetrics;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.TwoNodes;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.services.ServiceContext;

public class LayerWrapper extends Layer {

    private final MapContext mcontext;
    private final MapLayerInterface layer;
//    private final MapView mapView;

    public LayerWrapper(MapContext mc, MapLayerInterface l, MapView mv) {
        mcontext = mc;
        layer = l;
//        mapView = mv;
    }


    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        layer.drawInside(mcontext);
    }

    @Override
    public boolean onTap(LatLong laLo, Point lp, Point tp) {
        return layer.onTap(laLo, lp, tp);
    }


//    private class MapContextWrapper implements MapContext {
//        private final AndroidDraw draw =
//                new AndroidDraw(
//                        mcontext.getMetrics().getDensity(),
//                        mcontext.getContext().getResources());
//
//        private final MapsForgeMetrics metrics =
//                new MapsForgeMetrics(mapView, mcontext.getMetrics().getDensity());
//
//        public MapContextWrapper(BoundingBox b, byte z, Canvas canvas, Point tl) {
//            metrics.init(b, z, canvas, tl);
//            draw.init(canvas, mcontext.getMetrics());
//        }
//
//        @Override
//        public MapMetrics getMetrics() {
//            return metrics;
//        }
//
//        @Override
//        public MapDraw draw() {
//            return draw;
//        }
//
//        @Override
//        public ServiceContext getSContext() {
//            return mcontext.getSContext();
//        }
//
//        @Override
//        public Context getContext() {
//            return mcontext.getContext();
//        }
//
//        @Override
//        public String getSolidKey() {
//            return mcontext.getSolidKey();
//        }
//
//        @Override
//        public TwoNodes getTwoNodes() {
//            return mcontext.getTwoNodes();
//        }
//
//        @Override
//        public MapViewInterface getMapView() {
//            return mcontext.getMapView();
//        }
//    }

}
