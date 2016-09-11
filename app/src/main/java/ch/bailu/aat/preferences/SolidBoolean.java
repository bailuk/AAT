package ch.bailu.aat.preferences;



public class SolidBoolean extends SolidStaticIndexList {


    public SolidBoolean(Storage s, String k) {
        super(s, k, new String[] {
            "Disabled*",
            "Enabled*",
        });
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
