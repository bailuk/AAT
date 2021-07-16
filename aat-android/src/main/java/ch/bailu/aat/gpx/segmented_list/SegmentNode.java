package ch.bailu.aat.gpx.segmented_list;

import ch.bailu.aat.gpx.linked_list.Node;

public class SegmentNode extends Node {
    private final SegmentNode marker;

    private final Node first;
    private int count=0;



    public SegmentNode(Node n, SegmentNode m) {
        first=n;
        marker=m;
    }


    public SegmentNode(Node n) {
        this(n,null);
    }


    public Node getFirstNode() {
        return first;
    }


    public void update(Node n) {
        count++;
    }


    public int getSegmentSize() {
        return count;
    }


    public SegmentNode getMarker() {
        return marker;
    }
}
