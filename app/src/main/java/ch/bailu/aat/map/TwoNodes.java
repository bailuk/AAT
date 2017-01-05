package ch.bailu.aat.map;

import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.util.graphic.Pixel;

public class TwoNodes {
    private final MapMetrics metrics;

    public PixelNode
            nodeA=new PixelNode(),
            nodeB=new PixelNode();


    public class PixelNode {
        public Pixel pixel=new Pixel();
        public GpxPointInterface point= GpxPoint.NULL;

        public boolean isVisible() {
            return metrics.isVisible(point);
        }

        public void set(GpxPointInterface tp) {
            point=tp;
            pixel.setCopy(metrics.toPixel(tp));
        }
    }



    public TwoNodes(MapMetrics m) {
        metrics = m;
    }



    public boolean arePointsTooClose(int distance) {
        return (  Math.abs(nodeA.pixel.x - nodeB.pixel.x) +
                  Math.abs(nodeA.pixel.y - nodeB.pixel.y) < distance);


    }


    public void switchNodes() {
        PixelNode nodeT = nodeB;
        nodeB = nodeA;
        nodeA = nodeT;
    }
}
