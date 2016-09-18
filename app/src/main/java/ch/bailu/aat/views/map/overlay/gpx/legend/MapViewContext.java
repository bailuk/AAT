package ch.bailu.aat.views.map.overlay.gpx.legend;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.MapTwoNodes;
import ch.bailu.aat.views.map.overlay.MapTwoNodes.PixelNode;

public class MapViewContext {
    public static final int COLOR = 0x99ffffff;
    public static final int MIN_PIXEL_DISTANCE=100;

    public final MapPainter painter;
    public final MapTwoNodes nodes;

    
    public MapViewContext(MapPainter p, MapTwoNodes n) {
        painter=p;
        nodes=n;
    }
    
    public boolean isVisible(BoundingBox bounding) {
        return painter.projection.isVisible(bounding);
        
    }
    
    public void createDrawable() {
        painter.canvas.draw(painter.nodeBitmap, nodes.nodeA.pixel, COLOR);
    }

    

    public void drawNodeIfVisible(PixelNode node) {
        if (isVisible(node))
            drawNode(node);
    }

    
    public void drawNode(PixelNode node) {
        painter.canvas.draw(painter.nodeBitmap, node.pixel, COLOR);
    }
    
    public boolean isVisible(PixelNode node) {
        return painter.projection.isVisible(node.point);
    }
    
    
    public void drawLegend(PixelNode node, String text) {
        painter.canvas.drawText(text, node.pixel);
    }
    
    public boolean arePointsTooClose() {
        return nodes.arePointsTooClose(MIN_PIXEL_DISTANCE);
    }
}
