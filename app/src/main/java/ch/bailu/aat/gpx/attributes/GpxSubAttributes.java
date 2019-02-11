package ch.bailu.aat.gpx.attributes;

import ch.bailu.aat.gpx.GpxPointNode;

public abstract class GpxSubAttributes extends GpxAttributes {

    private final Keys keys;

    public GpxSubAttributes(Keys keys) {
        this.keys = keys;
    }

    public abstract boolean update(GpxPointNode p, boolean autoPause);



    @Override
    public int size() {
        return keys.size();
    }

    @Override
    public String getAt(int i) {
        return get(keys.getKeyIndex(i));
    }

    @Override
    public int getKeyAt(int i) {
        return keys.getKeyIndex(i);
    }


    @Override
    public boolean hasKey(int keyIndex) {
        return keys.hasKey(keyIndex);
    }

}
