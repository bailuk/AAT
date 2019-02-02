package ch.bailu.aat.gpx;

import ch.bailu.util_java.util.Objects;

public abstract class GpxAttributes {

    public final static GpxAttributes NULL = new GpxAttributesNull();
    public final static String NULL_VALUE="";


    public abstract String get(String key);
    public abstract String getValue(int index);
    public abstract String getKey(int index);

    public abstract void put(String key, String value);
    public abstract int size();
    public abstract void remove(String key);


    public float getFloatValue(int index) {
        return Objects.toFloat(getValue(index));
    }


    public long getLongValue(int index) {
        return Objects.toLong(getValue(index));
    }
}
