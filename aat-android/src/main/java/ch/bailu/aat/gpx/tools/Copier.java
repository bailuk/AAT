package ch.bailu.aat.gpx.tools;

import ch.bailu.aat.gpx.attributes.GpxListAttributes;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;

public class Copier extends GpxListWalker {
    private GpxList newList;

    private boolean newSegment = true;



    @Override
    public boolean doList(GpxList track) {
        newList = new GpxList(track.getDelta().getType(),
                GpxListAttributes.NULL);
        newSegment = true;
        return true;
    }

    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        newSegment = true;
        return true;
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return true;
    }

    @Override
    public void doPoint(GpxPointNode point) {
        if (newSegment) {
            newList.appendToNewSegment(point.getPoint(), point.getAttributes());

            newSegment = false;
        } else {
            newList.appendToCurrentSegment(point.getPoint(), point.getAttributes());
        }
    }


    public GpxList getNewList() {
        return newList;
    }
}
