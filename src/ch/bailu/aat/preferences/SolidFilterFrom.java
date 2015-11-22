package ch.bailu.aat.preferences;

import android.content.Context;

public class SolidFilterFrom extends SolidLong {
   private final static String KEY="filter_from"; 
    
    public SolidFilterFrom(Context context, int i) {
        super(Storage.preset(context), KEY+i);
    }

}
