package ch.bailu.aat_lib.gpx;

import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.linked_list.Node;

public class GpxPointLinkedNode extends GpxPointNode {
    public final static long SIZE_IN_BYTES=4;

    private float distance; //4byte

    public GpxPointLinkedNode(GpxPoint tp, GpxAttributes at) {
        super(tp, at);
    }

    @Override
    public void setPrevious(Node node) {
        super.setPrevious(node);
        distance = GpxDeltaHelper.getDistance((GpxPointNode)node, this);
    }

    @Override
    public float getAcceleration() {
        return GpxDeltaHelper.getAcceleration((GpxPointNode)getPrevious(), this);
    }

    @Override
    public float getDistance() {
        return distance;
    }

    @Override
    public float getSpeed() {
        return GpxDeltaHelper.getSpeed(
                distance,
                GpxDeltaHelper.getTimeDeltaSI(((GpxPointNode)getPrevious()), this));
    }

    @Override
    public long getTimeDelta() {
        return GpxDeltaHelper.getTimeDeltaMilli((GpxPointNode)getPrevious(), this);
    }

}
