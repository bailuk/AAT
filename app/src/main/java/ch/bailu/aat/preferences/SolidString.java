package ch.bailu.aat.preferences;

public class SolidString extends SolidType {
    private final String key;
    private final Storage storage;
        
        
    public SolidString(Storage s, String k) {
        storage=s;
        key=k;
    }

    @Override
    public String getValueAsString() {
        return getStorage().readString(getKey());
    }
        
    public void setValue(String v) {
        getStorage().writeString(key, v);
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
