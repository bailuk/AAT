package ch.bailu.aat.views.map.overlay;

import android.graphics.Point;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;

public class MapTwoNodes {
    private final MapPainter painter;
    
    public PixelNode nodeA=new PixelNode(), nodeB=new PixelNode();
    
    
    public class PixelNode {
        public final Point pixel=new Point();
        public GpxPointInterface point=GpxPoint.NULL;
        
        public boolean isVisible() {
            return painter.projection.isVisible(point);
        }

        public void set(GpxPointInterface tp) {
            point=tp;
            painter.projection.toPixels(tp,pixel);
        }
    }
    
    
    
    public MapTwoNodes(MapPainter p) {
        painter = p;
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
