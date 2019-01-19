package ch.bailu.aat.gpx;

public class GpxAttributesNull extends GpxAttributes {
    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public String getValue(int index) {
        return null;
    }

    @Override
    public String getKey(int index) {
        return null;
    }

    @Override
    public void put(String key, String value) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void remove(String key) {

    }
}
