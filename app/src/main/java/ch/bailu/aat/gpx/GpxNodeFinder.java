package ch.bailu.aat.gpx;

import ch.bailu.aat.coordinates.BoundingBoxE6;

public class GpxNodeFinder extends GpxListWalker {

    private final BoundingBoxE6 bounding;
    private GpxPointNode node;
    private int index=0;

    public GpxNodeFinder(BoundingBoxE6 b) {
        bounding = b;
    }


    @Override
    public boolean doList(GpxList s) {
        return BoundingBoxE6.doOverlap(s.getDelta().getBoundingBox(), bounding);
    }

    @Override
    public boolean doSegment(GpxSegmentNode s) {
        if (haveNode()) {
            return false;

        } else if (BoundingBoxE6.doOverlap(s.getBoundingBox(), bounding)) {
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
