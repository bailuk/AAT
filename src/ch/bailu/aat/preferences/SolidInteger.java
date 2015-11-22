package ch.bailu.aat.preferences;

public class SolidInteger extends SolidType {
    private final String key;
    private final Storage storage;
    
    
    public SolidInteger(Storage s, String k) {
        storage=s;
        key=k;
    }

    
    public int getValue() {
        return getStorage().readInteger(getKey());
    }
    
    
    public void setValue(int v) {
        getStorage().writeInteger(getKey(),v);
    }
    

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }
    
}
