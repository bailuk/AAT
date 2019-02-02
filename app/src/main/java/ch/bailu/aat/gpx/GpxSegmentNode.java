package ch.bailu.aat.gpx;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.gpx.linked_list.Node;
import ch.bailu.aat.gpx.segmented_list.SegmentNode;

public class GpxSegmentNode extends SegmentNode implements GpxBigDeltaInterface {

    private final GpxBigDelta delta=new GpxBigDelta(GpxTrackAttributes.factoryNull());

    
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
