package ch.bailu.aat.preferences;


import android.content.Context;

import ch.bailu.aat.R;

public class SolidBoolean extends SolidStaticIndexList {


    private static String[] label;


    private static String[] generateLabel(Context c) {
        if (label == null) {
            label = new String[] {
                    c.getString(R.string.off),
                    c.getString(R.string.on),
            };
        }
        return label;
    }

    public SolidBoolean(Storage s, String k) {
        super(s, k, generateLabel(s.getContext()));
    }

    public boolean isEnabled() {
        return getValue();
    }
    
    public boolean getValue() {
        return getIndex()==1;
    }
    
    public void setValue(boolean v) {
        if (v) setIndex(1);
        else   setIndex(0);
    }
    
}
