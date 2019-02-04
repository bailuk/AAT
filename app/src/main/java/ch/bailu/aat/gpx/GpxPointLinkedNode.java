package ch.bailu.aat.gpx;

import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.linked_list.Node;

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


    public float getAcceleration() {
        return GpxDeltaHelper.getAcceleration((GpxPointNode)getPrevious(), this);
    }


    public float getDistance() {
        return distance;
    }


    public float getSpeed() {
        return GpxDeltaHelper.getSpeed(
                distance,
                GpxDeltaHelper.getTimeDeltaSI(((GpxPointNode)getPrevious()), this));
    }


    public long getTimeDelta() {
        return GpxDeltaHelper.getTimeDeltaMilli((GpxPointNode)getPrevious(), this);
    }

}
