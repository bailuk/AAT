package ch.bailu.aat.preferences;

import ch.bailu.aat.R;

public class SolidGPSLock extends SolidBoolean {
    private static final String KEY = SolidGPSLock.class.getSimpleName();
    
    public SolidGPSLock(Storage s) {
        super(s, KEY);
    }

    @Override
    public String getValueAsString() {
        if (this.isEnabled()) return getContext().getString(R.string.gps_lock);
        else return "";
    }
}
