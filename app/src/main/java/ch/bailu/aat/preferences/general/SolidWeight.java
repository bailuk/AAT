package ch.bailu.aat.preferences.general;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidInteger;

public class SolidWeight extends SolidInteger {

    final private static String KEY="weight";
    
    
    public SolidWeight(Context c) {
        super(c, KEY);
        
    }
    
    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_weight_title);
    }

    public void setDefaults() {
        setValue(75);
    }


}
