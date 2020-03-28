package ch.bailu.aat.gpx;

import androidx.annotation.NonNull;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.attributes.Keys;
import ch.bailu.aat.gpx.interfaces.GpxDeltaPointInterface;
import ch.bailu.aat.gpx.linked_list.Node;


public abstract class GpxPointNode extends Node implements GpxDeltaPointInterface {

    private final GpxAttributes attributes;
    private final GpxPoint     point;


    public GpxPointNode(GpxPoint tp, GpxAttributes at) {
        point = tp;
        attributes = at;
    }


    public double getAltitude() {
        return point.getAltitude();
    }


    public double getLatitude() {
        return point.getLatitude();
    }


    public double getLongitude() {
        return point.getLongitude();
    }


    public long getTimeStamp() {
        return point.getTimeStamp();
    }


    public int getLatitudeE6() {
        return point.getLatitudeE6();
    }


    public int getLongitudeE6() {
        return point.getLongitudeE6();
    }


    @NonNull
    @Override
    public String toString() {
        return attributes.toString();
    }

    public GpxPoint getPoint() {
        return point;
    }

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
