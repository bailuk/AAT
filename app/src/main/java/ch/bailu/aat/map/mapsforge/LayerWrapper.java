package ch.bailu.aat.map.mapsforge;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.Layer;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;

public class LayerWrapper extends Layer {

    private final MapContext mcontext;
    private final MapLayerInterface layer;

    public LayerWrapper(MapContext mc, MapLayerInterface l) {
        mcontext = mc;
        layer = l;
    }


    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        layer.drawInside(mcontext);

    }

    @Override
    public boolean onTap(LatLong laLo, Point lp, Point tp) {
        return layer.onTap(laLo, lp, tp);
    }

}
