package ch.bailu.aat.gpx;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.attributes.Keys;
import ch.bailu.aat.gpx.interfaces.GpxDeltaPointInterface;
import ch.bailu.aat.gpx.linked_list.Node;


public abstract class GpxPointNode extends Node implements GpxDeltaPointInterface {

    private GpxAttributes attributes;
    private final GpxPoint     point;
    
    
    public GpxPointNode(GpxPoint tp, GpxAttributes at) {
        point = tp;
        attributes = at;
    }
    
    
    public short getAltitude() {
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
    

/*
    public String getAt(String key) {
        String value = attributes.get(key);
        if (value==null) {
            value=GpxAttributes.NULL_VALUE;
        }
        return value;
    }

    
    public void setValue(String key, int value) {
        setValue(key, String.valueOf(value));
    }
    
    
    public void setValue(String key, long value) {
        setValue(key, String.valueOf(value));
    }
    
    
    public void setValue(String key, double value) {
        setValue(key, String.valueOf(value));
    }
    
    public void setValue(String key, String value) {
        initMap();
        attributes.put(key, value);
    }
    

    private void initMap() {
        if (attributes == GpxAttributes.NULL) {
            attributes = new GpxAttributesStatic();
        }
    }

    
    public void removeKey(String key) {
        attributes.remove(key);
        releaseMap();
    }

    
    private void releaseMap() {
        if (attributes.size() == 0) {
            attributes = GpxAttributesStatic.NULL;
        }
    }
*/
    
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


    public void setAltitude(short e) {
        point.setAltitude(e);
    }
    

    private static int BOUNDING_KEY= Keys.toIndex("boundingbox");
    
        
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
