package ch.bailu.aat.map.mapsforge.layer.gpx.legend;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;
import ch.bailu.aat.map.mapsforge.layer.context.MapContextTwoNodes;

public class LegendContext {

    public static final int COLOR = 0x99ffffff;
    public static final int MIN_PIXEL_DISTANCE=100;

    public final MapContext painter;
    public final MapContextTwoNodes nodes;


    public LegendContext(MapContext c, MapContextTwoNodes n) {
        painter=c;
        nodes=n;
    }

    public boolean isVisible(BoundingBoxE6 bounding) {
        return painter.metrics.isVisible(bounding);

    }

    public void createDrawable() {
        painter.draw.bitmap(painter.draw.nodeBitmap.getTileBitmap(), nodes.nodeA.pixel, COLOR);
    }



    public void drawNodeIfVisible(MapContextTwoNodes.PixelNode node) {
        if (isVisible(node))
            drawNode(node);
    }


    public void drawNode(MapContextTwoNodes.PixelNode node) {
        painter.draw.bitmap(painter.draw.nodeBitmap.getTileBitmap(), node.pixel, COLOR);
    }

    public boolean isVisible(MapContextTwoNodes.PixelNode node) {
        return painter.metrics.isVisible(node.point);
    }


    public void drawLegend(MapContextTwoNodes.PixelNode node, String text) {
        painter.draw.text(text, node.pixel);
    }

    public boolean arePointsTooClose() {
        return nodes.arePointsTooClose(MIN_PIXEL_DISTANCE);
    }
}
