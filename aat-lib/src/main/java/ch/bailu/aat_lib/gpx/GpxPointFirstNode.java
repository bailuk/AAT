package ch.bailu.aat_lib.gpx;

import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;

public class GpxPointFirstNode extends GpxPointNode {

    public GpxPointFirstNode(GpxPoint tp, GpxAttributes at) {
        super(tp, at);
    }


    @Override
    public float getSpeed() {
        return 0f;
    }


    @Override
    public float getAcceleration() {
        return 0f;
    }


    @Override
    public float getDistance() {
        return 0f;
    }


    @Override
    public long getTimeDelta() {
        return 0;
    }

}
