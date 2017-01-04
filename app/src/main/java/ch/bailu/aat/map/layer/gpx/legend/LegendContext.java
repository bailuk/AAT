package ch.bailu.aat.map.layer.gpx.legend;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.TwoNodes;

public class LegendContext {

    public static final int COLOR = 0x99ffffff;
    public static final int MIN_PIXEL_DISTANCE=100;

    public final MapContext mcontext;
    public final TwoNodes nodes;


    public LegendContext(MapContext c, TwoNodes n) {
        mcontext =c;
        nodes=n;
    }

    public boolean isVisible(BoundingBoxE6 bounding) {
        return mcontext.getMetrics().isVisible(bounding);

    }

    public void createDrawable() {
        mcontext.draw().bitmap(mcontext.draw().getNodeBitmap(), nodes.nodeA.pixel, COLOR);
    }



    public void drawNodeIfVisible(TwoNodes.PixelNode node) {
        if (isVisible(node))
            drawNode(node);
    }


    public void drawNode(TwoNodes.PixelNode node) {
        mcontext.draw().bitmap(mcontext.draw().getNodeBitmap(), node.pixel, COLOR);
    }

    public boolean isVisible(TwoNodes.PixelNode node) {
        return mcontext.getMetrics().isVisible(node.point);
    }


    public void drawLegend(TwoNodes.PixelNode node, String text) {
        mcontext.draw().label(text, node.pixel);
    }

    public boolean arePointsTooClose() {
        return nodes.arePointsTooClose(MIN_PIXEL_DISTANCE);
    }
}
