package ch.bailu.aat.gpx;

import ch.bailu.aat.gpx.linked_list.Node;
import ch.bailu.aat.gpx.segmented_list.SegmentNode;

public class GpxListIterator {

    private class PointPrimerNode extends GpxPointFirstNode {
        public PointPrimerNode() {
            super(GpxPoint.NULL, GpxAttributesStatic.NULL_ATTRIBUTES);
        }
        
        @Override
        public Node getNext() {
            return track.getPointList().getFirst();
        }
    }

    
    private class SegmentPrimerNode extends GpxSegmentNode {
        public SegmentPrimerNode() {
            super(new PointPrimerNode());
        }
        
        @Override
        public Node getNext() {
            return track.getSegmentList().getFirst();
        }
    }
    
    private final GpxList track;

    private Node point = new PointPrimerNode();
    private Node segment = new SegmentPrimerNode();
    
    private int inSegmentIndex=-1;
    private int inTrackIndex=-1;
    
    public GpxListIterator(GpxList t) {
        track=t;
    }

    
    public boolean nextPoint() {
        if (setPoint(point.getNext())) {
            inSegmentIndex++;
            inTrackIndex++;
            
            if (inSegmentIndex == ((SegmentNode)segment).getSegmentSize()) 
                return nextSegment();
            
            return true;
        }
        return false;
    }
    
    
    private boolean setPoint(Node n) {
        if (n == null) return false;
        point = n;
        return true;
    }
    
    
    private boolean nextSegment() {
        if (setSegment(segment.getNext())) {
            inSegmentIndex=0;
            return true;
        }
        return false;
    }

    
    private boolean setSegment(Node n) {
        if (n == null) return false;
        segment = n;
        return true;
    }
    
    public GpxPointNode getPoint() {
        return (GpxPointNode)point;
    }


    public boolean isFirstInTrack() {
        return inTrackIndex==0;
    }
    
    public boolean isFirstInSegment() {
        return inSegmentIndex==0;
    }
}
