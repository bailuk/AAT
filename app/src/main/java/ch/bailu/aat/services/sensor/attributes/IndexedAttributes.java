package ch.bailu.aat.services.sensor.attributes;

import ch.bailu.aat.gpx.GpxAttributes;

public abstract class IndexedAttributes extends GpxAttributes {

    private final String[] keys;

    public IndexedAttributes(String[] k) {
        keys = k;
    }

    @Override
    public String get(String key) {
        for (int i = 0; i< keys.length; i++) {
            if (key.equalsIgnoreCase(keys[i])) return getValue(i);
        }

        return "";
    }

    @Override
    public String getKey(int index) {
        if (index < keys.length) return keys[index];
        return "";
    }

    @Override
    public void put(String key, String value) {

    }

    @Override
    public int size() {
        return keys.length;
    }

    @Override
    public void remove(String key) {

    }




}
