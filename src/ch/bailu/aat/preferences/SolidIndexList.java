package ch.bailu.aat.preferences;

public abstract class SolidIndexList extends SolidType {

    private static final int DEFAULT_IMAGE_RESOURCE = 0;
    private final SolidInteger sindex;
    
    
    public SolidIndexList(Storage s, String k) {
        sindex = new SolidInteger(s,k);
    }
    
    
    public abstract int length();

    
    public abstract String getString();

    
    public void setIndex(int i) {
        sindex.setValue(i);
    }

    
    public abstract String[] getStringArray();
    
    
    public int getIndex() {
        return sindex.getValue();
    }

    
    @Override
    public String getKey() {
        return sindex.getKey();
    }


    @Override
    public Storage getStorage() {
        return sindex.getStorage();
    }


    public void cycle() {
        int index=getIndex()+1;
        if (index == length()) index=0;
        setIndex(index);
    }

    
    public int getImageResource() {
        return DEFAULT_IMAGE_RESOURCE;
    }
}
