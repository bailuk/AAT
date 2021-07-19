package ch.bailu.aat_lib.preferences;


import ch.bailu.aat_lib.resources.Res;

public class SolidBoolean extends SolidStaticIndexList {


    private static String[] label;


    private static String[] generateLabel() {
        if (label == null) {
            label = new String[] {
                    Res.str().off(),
                    Res.str().on(),
            };
        }
        return label;
    }

    public SolidBoolean(StorageInterface s, String k) {
        super(s, k, generateLabel());
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
