package ch.bailu.aat.preferences;

import ch.bailu.aat.R;
import android.content.Context;

public class SolidMET extends SolidStaticIndexList {
    private final static String KEY="met";
    
    
    
    public SolidMET(Context c, int i) {
        super(Storage.preset(c), KEY+i, c.getResources().getStringArray(R.array.p_met_list));
        
    }

    public float getMETValue() {
        return Float.valueOf(getString().substring(0, 4));
    }
    
    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_met);
    }
    
}
