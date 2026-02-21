package ch.bailu.aat_lib.gpx;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.attributes.Keys;
import ch.bailu.aat_lib.gpx.interfaces.GpxDeltaPointInterface;
import ch.bailu.aat_lib.gpx.linked_list.Node;

public abstract class GpxPointNode extends Node implements GpxDeltaPointInterface {

    private final GpxAttributes attributes;
    private final GpxPoint     point;


    public GpxPointNode(GpxPoint tp, GpxAttributes at) {
        point = tp;
        attributes = at;
    }

    @Override
    public float getAltitude() {
        return point.getAltitude();
    }

    @Override
    public double getLatitude() {
        return point.getLatitude();
    }

    @Override
    public double getLongitude() {
        return point.getLongitude();
    }

    @Override
    public long getTimeStamp() {
        return point.getTimeStamp();
    }

    @Override
    public int getLatitudeE6() {
        return point.getLatitudeE6();
    }

    @Override
    public int getLongitudeE6() {
        return point.getLongitudeE6();
    }


    @Nonnull
    @Override
    public String toString() {
        return attributes.toString();
    }

    public GpxPoint getPoint() {
        return point;
    }

    @Override
    public GpxAttributes getAttributes() {
        return attributes;
    }

    public void setAltitude(double e) {
        point.setAltitude((float) e);
    }


    private static final int BOUNDING_KEY= Keys.toIndex("boundingbox");


    @Override
    public BoundingBoxE6 getBoundingBox() {
        final BoundingBoxE6 box = new BoundingBoxE6();

        if (attributes.hasKey(BOUNDING_KEY)) {
            box.add(attributes.get(BOUNDING_KEY));
        }
        box.add(getPoint());

        return box;
    }
}
