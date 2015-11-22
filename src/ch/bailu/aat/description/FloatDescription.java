package ch.bailu.aat.description;

import android.content.Context;


public abstract class FloatDescription extends ContentDescription {

    private float cache=0f;
    
    public FloatDescription(Context c) {
        super(c);
    }

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
