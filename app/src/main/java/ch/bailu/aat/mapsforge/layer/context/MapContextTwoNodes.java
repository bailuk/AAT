package ch.bailu.aat.mapsforge.layer.context;

import android.graphics.Point;

import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.mapsforge.layer.context.MapContextMetrics;

public class MapContextTwoNodes {
    private final MapContextMetrics metrics;

    public PixelNode
            nodeA=new PixelNode(),
            nodeB=new PixelNode();


    public class PixelNode {
        public Point pixel=new Point();
        public GpxPointInterface point= GpxPoint.NULL;

        public boolean isVisible() {
            return metrics.isVisible(point);
        }

        public void set(GpxPointInterface tp) {
            point=tp;
            pixel = metrics.toPixel(tp);
        }
    }



    public MapContextTwoNodes(MapContextMetrics m) {
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
