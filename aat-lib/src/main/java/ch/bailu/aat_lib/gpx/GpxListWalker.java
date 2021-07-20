package ch.bailu.aat_lib.gpx;

import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.gpx.linked_list.Node;
import ch.bailu.aat_lib.gpx.segmented_list.SegmentNode;


public abstract class GpxListWalker {
    private int increment=0;
    private int count=0;


    public void walkTrack(GpxList track, int inc) {
        increment = inc-1;

        walkTrack(track);
    }


    public void walkTrack(GpxList track) {
        increment=0;
        if (doList(track)) {

            Node segment = track.getSegmentList().getFirst();

            while (segment != null) {
                walkSegment((GpxSegmentNode)segment);
                segment = segment.getNext();
            }
        }
    }


    private void walkSegment(GpxSegmentNode segment) {
        if (doSegment(segment)) {
            int count = segment.getSegmentSize();
            Node marker = segment.getMarker();

            while( count > 0 ) {
                walkMarker((GpxSegmentNode) marker);

                count -= ((SegmentNode)marker).getSegmentSize();
                marker=marker.getNext();
            }
        }
    }


    private void walkMarker(GpxSegmentNode marker) {
        if (doMarker(marker)) {
            int count = marker.getSegmentSize();

            Node node = marker.getFirstNode();

            while ( count > 0 ) {

                if (this.count == 0) {
                    this.count=increment;
                    doPoint((GpxPointNode)node);
                } else {
                    this.count--;
                }

                count --;
                node = node.getNext();

            }
        }
    }


    public abstract boolean doList(GpxList track);
    public abstract boolean doSegment(GpxSegmentNode segment);
    public abstract boolean doMarker(GpxSegmentNode marker);
    public abstract void doPoint(GpxPointNode point);
}
