package ch.bailu.aat.gpx;

import ch.bailu.aat.coordinates.BoundingBox;

public class GpxNodeFinder extends GpxListWalker {

    private final BoundingBox bounding;
    private GpxPointNode node;
    private int index=-1;
    
    public GpxNodeFinder(BoundingBox b) {
        bounding = b;
    }
    
    
    @Override
    public boolean doList(GpxList s) {
        if (BoundingBox.doOverlap(s.getDelta().getBoundingBox(), bounding)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean doSegment(GpxSegmentNode s) {
        if (haveNode() == false && BoundingBox.doOverlap(s.getBoundingBox(), bounding)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean doMarker(GpxSegmentNode s) {
        if (haveNode() == false && BoundingBox.doOverlap(s.getBoundingBox(), bounding)) {
            return true;
        }
        return false;
    }

    @Override
    public void doPoint(GpxPointNode point) {
        if (haveNode() == false) {
            index++;
            if (bounding.contains(point)) {
                node = point;
            }
        }
    }
    
    public boolean haveNode() {
        return node != null;
    }


    public GpxPointNode getNode() {
        return node;
    }
    
    public int getNodeIndex() {
        return index;
    }
}
