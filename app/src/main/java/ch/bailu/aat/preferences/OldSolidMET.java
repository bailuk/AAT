package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;

public class OldSolidMET extends SolidStaticIndexList {
    private final static String KEY="met";
    
    
    
    public OldSolidMET(Context c, int i) {
        super(Storage.preset(c), KEY+i, c.getResources().getStringArray(R.array.p_met_list));
        
    }

    public float getMETValue() {
        return Float.valueOf(getValueAsString().substring(0, 4));
    }
    
    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_met);
    }


}
