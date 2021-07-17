package ch.bailu.aat.description;

import ch.bailu.aat_lib.description.ContentDescription;


public abstract class FloatDescription extends ContentDescription {

    private float cache=0f;

    protected float getCache() {
        return cache;
    }

    protected boolean setCache(float f) {
        boolean r;
        if (cache==f) {
            r=false;
        } else {
            r=true;
            cache=f;
        }
        return r;
    }
}
