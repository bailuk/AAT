package ch.bailu.aat.gpx;

import ch.bailu.aat.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.gpx.linked_list.List;
import ch.bailu.aat.gpx.linked_list.Node;
import ch.bailu.aat.gpx.segmented_list.SegmentNode;
import ch.bailu.aat.gpx.segmented_list.SegmentNodeFactory;
import ch.bailu.aat.gpx.segmented_list.SegmentedList;

public class GpxList {
    public static final GpxList NULL_TRACK = new GpxList(GpxType.TRK, MaxSpeed.NULL, AutoPause.NULL);
    public static final GpxList NULL_ROUTE = new GpxList(GpxType.RTE, MaxSpeed.NULL, AutoPause.NULL);

    


    private final static SegmentNodeFactory GPX_SEGMENT_FACTORY = new SegmentNodeFactory () {
        @Override
        public SegmentNode createMarker(Node n) {
            return new GpxSegmentNode((GpxPointNode)n);
        }
        @Override
        public SegmentNode createSegment(Node n, SegmentNode m) {
            return new GpxSegmentNode((GpxPointNode)n,(GpxSegmentNode)m);
        }
    };





    private final SegmentedList list = new SegmentedList(GPX_SEGMENT_FACTORY);
    private final GpxBigDelta delta;

    public GpxList(int type, MaxSpeed max, AutoPause pause) {
        delta = new GpxBigDelta(max, pause);
        delta.setType(type);
    }


    public void appendToCurrentSegment(GpxPoint tp, GpxAttributes at) {
        if (list.getSegmentList().size()==0) {
            appendToNewSegment(tp, at);
        } else {
            GpxPointLinkedNode n = new GpxPointLinkedNode(tp, at);
            list.appendToCurrentSegment(n);
            delta.update(n);
        }
    }


    public void appendToNewSegment(GpxPoint tp, GpxAttributes at) {
        GpxPointNode n = new GpxPointFirstNode(tp, at);
        list.appendToNewSegment(n);
        delta.updateWithPause(n);
    }


    public List getPointList() {
        return list.getPointList();
    }

    public List getSegmentList() {
        return list.getSegmentList();
    }


    public List getMarkerList() {
        return list.getMarkerList();
    }


    public GpxBigDeltaInterface getDelta() {
        return delta;
    }


    public void setType(int type) {
        delta.setType(type);
    }


}
