package ch.bailu.aat_lib.description;

public abstract class DoubleDescription extends ContentDescription {

    private double cache;

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