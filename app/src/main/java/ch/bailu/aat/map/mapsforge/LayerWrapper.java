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
import ch.bailu.aat.util.ui.AppLog;

public class LayerWrapper extends Layer {

    private final MapContext mcontext;
    private final MapLayerInterface layer;

    public LayerWrapper(MapContext mc, MapLayerInterface l) {
        mcontext = mc;
        layer = l;
    }


    @Override
    public void draw(BoundingBox bounding, byte zoom, Canvas canvas, Point topLeftPoint) {
        if (mcontext.getSContext().lock()) {
            layer.drawInside(mcontext);
            mcontext.getSContext().free();
        }
    }

    @Override
    public boolean onTap(LatLong laLo, Point lp, Point tp) {
        return layer.onTap(laLo, lp, tp);
    }

}
