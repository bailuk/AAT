package ch.bailu.aat.map.mapsforge;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.Layer;

import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat.map.To;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat.services.InsideContext;

public class LayerWrapper extends Layer {

    private final MapContext mcontext;
    private final MapLayerInterface layer;

    public LayerWrapper(MapContext mc, MapLayerInterface l) {
        mcontext = mc;
        layer = l;
    }


    @Override
    public void draw(BoundingBox bounding, byte zoom, Canvas canvas, Point topLeftPoint) {
        new InsideContext(To.scontext(mcontext)) {
            @Override
            public void run() {
                layer.drawInside(mcontext);
            }
        };
    }
}
