package ch.bailu.aat.description;

import android.content.Context;

public abstract class DoubleDescription extends ContentDescription {

    private double cache;

    public DoubleDescription(Context c) {
        super(c);
    }

    protected double getCache() {
        return cache;
    }

    protected boolean setCache(double f) {
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
