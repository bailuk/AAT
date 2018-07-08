package ch.bailu.aat.map.layer.gpx.legend;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.map.MapColor;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.TwoNodes;

public class LegendContext {


    public static final int MIN_DI_PIXEL_DISTANCE=100;
    public final int min_pixel_distance;

    private final MapContext mcontext;
    public final TwoNodes nodes;


    public LegendContext(MapContext mc) {
        mcontext = mc;
        nodes=mc.getTwoNodes();
        min_pixel_distance = mc.getMetrics().getDensity().toPixel_i(MIN_DI_PIXEL_DISTANCE);
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


    public void drawLabelA(String text) {
        drawLabel(nodes.nodeA, text);
    }


    public void drawLabelB(String text) {
        drawLabel(nodes.nodeB, text);
    }



    private void drawLabel(TwoNodes.PixelNode node, String text) {
        mcontext.draw().label(text, node.pixel);
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
