package ch.bailu.aat_lib.gpx.segmented_list;

import ch.bailu.aat_lib.gpx.linked_list.List;
import ch.bailu.aat_lib.gpx.linked_list.Node;

public class SegmentedList {
    private static final int DEFAULT_MARKER_SIZE=200;

    private final SegmentNodeFactory factory;

    private final List segment = new List();
    private final List marker = new List();
    private final List entry = new List();


    public SegmentedList() {
        factory = SegmentNodeFactory.DEFAULT_FACTORY;
    }


    public SegmentedList(SegmentNodeFactory f) {
        factory = f;
    }


    public void appendToCurrentSegment(Node n) {
        entry.append(n);
        addMarker(n);

        ((SegmentNode) marker.getLast()).update(n);
        ((SegmentNode) segment.getLast()).update(n);
    }


    private void addMarker(Node n) {
        SegmentNode m = (SegmentNode) marker.getLast();

        if (m==null || m.getSegmentSize() >= DEFAULT_MARKER_SIZE) {
            marker.append(factory.createMarker(n));
        }

    }


    public void appendToNewSegment(Node n) {
        SegmentNode m=factory.createMarker(n);
        SegmentNode s=factory.createSegment(n,m);

        marker.append(m);
        segment.append(s);
        entry.append(n);

        m.update(n);
        s.update(n);

    }


    public List getPointList() {
        return entry;
    }


    public List getMarkerList() {
        return marker;
    }


    public List getSegmentList() {
        return segment;
    }
}
