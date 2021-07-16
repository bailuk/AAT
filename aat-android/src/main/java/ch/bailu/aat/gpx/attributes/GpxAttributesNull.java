package ch.bailu.aat.gpx.attributes;

public class GpxAttributesNull extends GpxAttributes {

    public final static GpxAttributes NULL = new GpxAttributesNull();

    @Override
    public String get(int keyIndex) {
        return NULL_VALUE;
    }

    @Override
    public boolean hasKey(int keyIndex) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public String getAt(int i) {
        return NULL_VALUE;
    }

    @Override
    public int getKeyAt(int i) {
        return 0;
    }
}
