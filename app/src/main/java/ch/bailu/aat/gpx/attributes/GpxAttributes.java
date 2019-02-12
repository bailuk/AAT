package ch.bailu.aat.gpx.attributes;

import ch.bailu.util_java.util.Objects;

public abstract class GpxAttributes {

    public final static GpxAttributes NULL = new GpxAttributesNull();
    public final static String NULL_VALUE="";


    public abstract String get(int keyIndex);
    public abstract boolean hasKey(int keyIndex);

    public abstract int size();

    public abstract String getAt(int i);
    public abstract int getKeyAt(int i);
    public String getSKeyAt(int i) {
        return Keys.toString(getKeyAt(i));
    }


    public void put(int keyIndex, String value) {

    }


    public float getAsFloat(int keyIndex) {
        return Objects.toFloat(get(keyIndex));
    }
    public long getAsLong(int keyIndex) {
        return Objects.toLong(get(keyIndex));
    }

    public int getAsInteger(int keyIndex) {
        return Objects.toInt(get(keyIndex));
    }

    public boolean getAsBoolean(int keyIndex) { return Objects.toBoolean(get(keyIndex));}


}
