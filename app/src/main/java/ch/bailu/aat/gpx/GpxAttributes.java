package ch.bailu.aat.gpx;

public abstract class GpxAttributes {

    public final static GpxAttributes NULL = new GpxAttributesNull();
    public final static String NULL_VALUE="0";


    public abstract String get(String key);
    public abstract String getValue(int index);
    public abstract String getKey(int index);

    public abstract void put(String key, String value);
    public abstract int size();
    public abstract void remove(String key);
}
