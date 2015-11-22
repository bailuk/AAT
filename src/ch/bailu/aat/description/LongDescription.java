package ch.bailu.aat.description;

import android.content.Context;


public abstract class LongDescription extends ContentDescription {

    public LongDescription(Context c) {
        super(c);
    }

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
