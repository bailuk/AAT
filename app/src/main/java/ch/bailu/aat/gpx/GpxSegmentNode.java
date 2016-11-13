package ch.bailu.aat.gpx;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat.gpx.linked_list.Node;
import ch.bailu.aat.gpx.segmented_list.SegmentNode;

public class GpxSegmentNode extends SegmentNode implements GpxBigDeltaInterface {

    private final GpxBigDelta delta=new GpxBigDelta(new MaxSpeed.Raw());

    
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


    public float getMaximumSpeed() {
        return delta.getMaximumSpeed();
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


    public BoundingBox getBoundingBox() {
        return delta.getBoundingBox();
    }


    @Override
    public long getEndTime() {
        return delta.getEndTime();
    }


    @Override
    public double getBearing() {
        return 0;
    }


	@Override
	public int getType() {
		return delta.getType();
	}

}
