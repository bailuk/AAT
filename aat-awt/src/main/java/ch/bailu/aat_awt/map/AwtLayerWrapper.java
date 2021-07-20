package ch.bailu.aat_awt.map;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.Layer;

import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;

public class AwtLayerWrapper extends Layer {
    private final MapLayerInterface layer;
    private final MapContext mcontext;


    public AwtLayerWrapper(MapContext mc, MapLayerInterface l) {
        mcontext = mc;
        layer = l;
    }

    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        layer.drawInside(mcontext);
    }
}
