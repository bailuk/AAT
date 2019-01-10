package ch.bailu.aat.preferences.map;


import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidBoolean;

public class SolidPositionLock extends SolidBoolean {
    private static final String POSTFIX = "_POSITION_LOCK";
    
    public SolidPositionLock(Context c, String k) {
        super(c, k + POSTFIX);
    }
    
    
    @Override
    public int getIconResource() {
        if (getValue()==true) {
            return R.drawable.zoom_original_inverse;
            
        } else {
            return R.drawable.zoom_original;
        }
    }
    
    @Override
    public String getValueAsString() {
        return getContext().getString(R.string.tt_map_home);
    }
}
