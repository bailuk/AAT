package ch.bailu.aat_lib.description;

public abstract class LongDescription extends ContentDescription {

    private long cache=0;


    protected long getCache() {
        return cache;
    }

    protected boolean setCache(long v) {
        boolean r;
        if (cache==v) {
            r=false;
        } else {
            r=true;
            cache=v;
        }
        return r;
    }

}
