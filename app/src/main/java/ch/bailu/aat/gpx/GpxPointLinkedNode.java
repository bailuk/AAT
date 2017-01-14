package ch.bailu.aat.gpx;

import ch.bailu.aat.gpx.linked_list.Node;

public class GpxPointLinkedNode extends GpxPointNode {
    public final static long SIZE_IN_BYTES=4;

    private float distance; //4byte
    //private float speed;    //4byte

    public GpxPointLinkedNode(GpxPoint tp, GpxAttributes at) {
        super(tp, at);
    }

    @Override
    public void setPrevious(Node node) {
        super.setPrevious(node);
        distance = GpxDeltaHelper.getDistance((GpxPointNode)node, this);
        /*speed = GpxDeltaHelper.getSpeed(
                distance,GpxDeltaHelper.getTimeDeltaSI(((GpxPointNode)node), this));*/
    }


    public float getAcceleration() {
        return GpxDeltaHelper.getAcceleration((GpxPointNode)getPrevious(), this);
    }


    public float getDistance() {
        return distance;
    }


    public float getSpeed() {
        //return speed;
        return GpxDeltaHelper.getSpeed(
                distance,
                GpxDeltaHelper.getTimeDeltaSI(((GpxPointNode)getPrevious()), this));
    }


    public long getTimeDelta() {
        return GpxDeltaHelper.getTimeDeltaMilli((GpxPointNode)getPrevious(), this);
    }


//    @Override
//    public double getBearing() {
//        return GpxDeltaHelper.getBearing((GpxPointNode)getPrevious(), this);
//    }
}
