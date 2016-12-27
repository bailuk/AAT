package ch.bailu.aat.gpx;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.interfaces.GpxDeltaPointInterface;
import ch.bailu.aat.gpx.linked_list.Node;


public abstract class GpxPointNode extends Node implements GpxDeltaPointInterface {
    private final static String NULL_VALUE="0";

    private GpxAttributes attributes = GpxAttributesStatic.NULL_ATTRIBUTES;
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
    
    
    
    public int getIntValue(String key) {
        return Integer.valueOf(getValue(key));
    }
    
    public double getDoubleValue(String key) {
        return Double.valueOf(getValue(key));
    }


    public String getValue(String key) {
        String value = attributes.get(key);
        if (value==null) {
            value=NULL_VALUE;
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
        if (attributes == GpxAttributesStatic.NULL_ATTRIBUTES) {
            attributes = new GpxAttributesStatic();
        }
    }

    
    public void removeKey(String key) {
        attributes.remove(key);
        releaseMap();
    }

    
    private void releaseMap() {
        if (attributes.size() == 0) {
            attributes = GpxAttributesStatic.NULL_ATTRIBUTES;
        }
    }

    
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
    
    
    
        
    @Override
    public BoundingBox getBoundingBox() {
        final BoundingBox  box = new BoundingBox();
        
        String bounding = attributes.get("boundingbox");
        if (bounding != null) {
            box.add(bounding);
        }
        
        box.add(getPoint());
        
        return box;
    }
}
