package ch.bailu.aat_lib.gpx;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes;
import ch.bailu.aat_lib.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.gpx.linked_list.Node;
import ch.bailu.aat_lib.gpx.segmented_list.SegmentNode;

public class GpxSegmentNode extends SegmentNode implements GpxBigDeltaInterface {

    private final GpxBigDelta delta=new GpxBigDelta(GpxListAttributes.NULL);


    public GpxSegmentNode(GpxPointNode n) {
        super(n);
    }


    public GpxSegmentNode(GpxPointNode n, GpxSegmentNode m) {
        super(n, m);
    }




    @Override
    public void update(Node n) {
        GpxPointNode node=(GpxPointNode)n;

        super.update(node);
        delta.update(node);
    }


    public float getAcceleration() {
        return delta.getAcceleration();
    }


    public float getSpeed() {
        return delta.getSpeed();
    }


    public float getDistance() {
        return delta.getDistance();
    }



    public long getPause() {
        return delta.getPause();
    }

    public long getStartTime() {
        return delta.getStartTime();
    }


    public long getTimeDelta() {
        return delta.getTimeDelta();
    }


    public BoundingBoxE6 getBoundingBox() {
        return delta.getBoundingBox();
    }


    @Override
    public long getEndTime() {
        return delta.getEndTime();
    }



    @Override
    public GpxType getType() {
        return delta.getType();
    }

    @Override
    public GpxAttributes getAttributes() {
        return delta.getAttributes();
    }

}
