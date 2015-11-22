package ch.bailu.aat.preferences;

import android.content.Context;

public class SolidFilterTo extends SolidLong {
    private final static String KEY="filter_to"; 
    
    public SolidFilterTo(Context context, int i) {
        super(Storage.preset(context), KEY+i);
    }

}
