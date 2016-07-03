package ch.bailu.aat.preferences;

public class SolidGPSLock extends SolidBoolean {
    private static final String KEY = SolidGPSLock.class.getSimpleName();
    
    public SolidGPSLock(Storage s) {
        super(s, KEY);
    }

    @Override
    public String getString() {
        if (this.isEnabled()) return "locked*";
        else return "";
    }
}
