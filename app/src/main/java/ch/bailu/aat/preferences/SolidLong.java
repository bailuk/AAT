package ch.bailu.aat.preferences;

public class SolidLong extends SolidType {


    private final String key;
    private final Storage storage;
        
        
    public SolidLong(Storage s, String k) {
        storage=s;
        key=k;
    }

    public long getValue() {
        return getStorage().readLong(getKey());
    }
        
    public void setValue(long v) {
        getStorage().writeLong(getKey(),v);
    }
        

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public String getValueAsString() {
        return String.valueOf(getValue());
    }
}
