package ch.bailu.aat.views.map.overlay.gpx;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.MapTwoNodes;
import ch.bailu.aat.views.map.overlay.MapTwoNodes.PixelNode;


public abstract class GpxListPainter extends GpxListWalker {
    private static final int MIN_PIXEL_SPACE=10;
    private static final int MAX_PIXEL_SPACE=100;

    private final static int START_PAINTING=0;
    private final static int CONTINUE_PAINTING=2;


    private final MapPainter painter;
    
    private final float minDistance, maxDistance;
    private float distanceCount;
    

    private int action = START_PAINTING; 


    public GpxListPainter (MapPainter p) {
        painter=p;
        minDistance = painter.projection.getDistanceFromPixel(MIN_PIXEL_SPACE);
        maxDistance = painter.projection.getDistanceFromPixel(MAX_PIXEL_SPACE);
        
    }


    public abstract void drawEdge(MapTwoNodes nodes);
    public abstract void drawNode(PixelNode node);

    
    @Override
    public boolean doList(GpxList track) {
        distanceCount=0;
        return painter.projection.isVisible(track.getDelta().getBoundingBox());
    }


    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        restartPainting();
        return painter.projection.isVisible(segment.getBoundingBox());
    }


    private void restartPainting() {
        action=START_PAINTING;
        distanceCount=0;
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        if (painter.projection.isVisible(marker.getBoundingBox())) {
            
            if (action == START_PAINTING) {
                doFirstNode(marker);

            }

            if (marker.getDistance() < minDistance) {
                doMarkerFirstNode(marker);
                return false;
            }
            
            
            return true;

        } else {
            restartPainting();
            return false;
        }
    }






    private void doFirstNode(GpxSegmentNode marker) {
        if (marker.getFirstNode() != null) {
            action = CONTINUE_PAINTING;
            
            painter.nodes.nodeA.set((GpxPointNode)marker.getFirstNode());
            drawNode(painter.nodes.nodeA);
            
            distanceCount=0;
        }
    }

    
    private void doMarkerFirstNode(GpxSegmentNode marker) {
        if (marker.getFirstNode() != null) {
            drawNodeIfDistance((GpxPointInterface)marker.getFirstNode());
            
            distanceCount += marker.getDistance();
        }
    }
    
    
    @Override
    public void doPoint(GpxPointNode point) {
        distanceCount += point.getDistance();
        drawNodeIfDistance(point);
    }

    
    private void drawNodeIfDistance(GpxPointInterface point) {
        if (distanceCount > minDistance) {
            drawNodeIfVisible(point);
            distanceCount=0;
        }
    }
    
    
    private void drawNodeIfVisible(GpxPointInterface point) {
        
        painter.nodes.nodeB.set(point);
        
        if (painter.nodes.nodeB.isVisible()) {
            drawEdge(painter.nodes);
            drawNode(painter.nodes.nodeB);

        } else if (painter.nodes.nodeA.isVisible()) {
            drawEdge(painter.nodes);
            
        } else if (distanceCount > maxDistance) {
            drawEdge(painter.nodes);
        }
        
        painter.nodes.switchNodes();
    }
    
}
