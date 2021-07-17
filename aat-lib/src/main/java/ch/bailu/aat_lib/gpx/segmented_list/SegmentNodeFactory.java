package ch.bailu.aat_lib.gpx.segmented_list;


import ch.bailu.aat_lib.gpx.linked_list.Node;

public abstract class SegmentNodeFactory {
    public static final SegmentNodeFactory DEFAULT_FACTORY = new SegmentNodeFactory() {

        @Override
        public SegmentNode createMarker(Node n) {
            return new SegmentNode(n);
        }

        @Override
        public SegmentNode createSegment(Node n, SegmentNode m) {
            return new SegmentNode(n, m);
        }

    };


    public abstract SegmentNode createMarker(Node n);
    public abstract SegmentNode createSegment(Node n, SegmentNode m);
}
