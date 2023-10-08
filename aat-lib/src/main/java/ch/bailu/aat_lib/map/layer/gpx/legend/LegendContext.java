package ch.bailu.aat_lib.map.layer.gpx.legend;

import org.mapsforge.core.graphics.Paint;

import ch.bailu.aat_lib.map.MapColor;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.TwoNodes;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.gpx.GpxPointNode;

public final class LegendContext {


    public static final int MIN_DI_PIXEL_DISTANCE=100;
    public final int min_pixel_distance;

    private final MapContext mcontext;
    public final TwoNodes nodes;

    private final Paint backgroundPaint, framePaint;

    public LegendContext(MapContext mcontext, Paint background, Paint frame) {
        this.mcontext = mcontext;
        nodes= mcontext.getTwoNodes();
        min_pixel_distance = mcontext.getMetrics().getDensity().toPixelInt(MIN_DI_PIXEL_DISTANCE);
        backgroundPaint = background;
        framePaint = frame;
    }

    public boolean isVisible(BoundingBoxE6 bounding) {
        return mcontext.getMetrics().isVisible(bounding);
    }


    public void drawNodeB() {
        drawNode(nodes.nodeB);
    }

    public void drawNodeA() {
        drawNode(nodes.nodeA);
    }


    private void drawNode(TwoNodes.PixelNode node) {
        mcontext.draw().bitmap(mcontext.draw().getNodeBitmap(), node.pixel, MapColor.NODE_NEUTRAL);
    }


    public void setA(GpxPointNode point) {
        nodes.nodeA.set(point);
    }

    public void setB(GpxPointNode point) {
        nodes.nodeB.set(point);
    }


    public void drawLabelB(String text) {
        drawLabel(nodes.nodeB, text);
    }



    private void drawLabel(TwoNodes.PixelNode node, String text) {
        mcontext.draw().label(text, node.pixel, backgroundPaint, framePaint);
    }


    public boolean arePointsTooClose() {
        return nodes.arePointsTooClose(min_pixel_distance);
    }

    public void switchNodes() {
        nodes.switchNodes();
    }


    public boolean isAVisible() {
        return isVisible(nodes.nodeA);
    }

    public boolean isBVisible() {
        return isVisible(nodes.nodeB);
    }

    private boolean isVisible(TwoNodes.PixelNode node) {
        return mcontext.getMetrics().isVisible(node.point);
    }

}
