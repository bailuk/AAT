package ch.bailu.aat.gpx;

import ch.bailu.aat.coordinates.BoundingBox;

public class GpxNodeFinder extends GpxListWalker {

    private final BoundingBox bounding;
    private GpxPointNode node;
    private int index=0;

    public GpxNodeFinder(BoundingBox b) {
        bounding = b;
    }


    @Override
    public boolean doList(GpxList s) {
        return BoundingBox.doOverlap(s.getDelta().getBoundingBox(), bounding);
    }

    @Override
    public boolean doSegment(GpxSegmentNode s) {
        if (haveNode()) {
            return false;

        } else if (BoundingBox.doOverlap(s.getBoundingBox(), bounding)) {
            return true;

        } else {
            index = index + s.getSegmentSize();
            return false;
        }
    }

    @Override
    public boolean doMarker(GpxSegmentNode s) {
        return doSegment(s);
    }

    @Override
    public void doPoint(GpxPointNode point) {
        if (haveNode() == false) {
            if (bounding.contains(point)) {
                node = point;
            } else {
                index++;
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
