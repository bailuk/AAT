package ch.bailu.aat.preferences;

public abstract class SolidIndexList extends SolidType {

    private static final int DEFAULT_IMAGE_RESOURCE = 0;
    private final SolidInteger sindex;
    
    
    public SolidIndexList(Storage s, String k) {
        sindex = new SolidInteger(s,k);
    }
    
    
    public abstract int length();
    public abstract String getValueAsString(int i);

    public String getValueAsString() {
        return getValueAsString(getIndex());
    }
    public void setIndex(int i) {
        sindex.setValue(validate(i));
    }


    private int validate(int i) {
        if (i < 0) i = length()-1;
        else if (i >= length()) i=0;
        return i;
    }


    public String[] getStringArray() {
        String[] r = new String[length()];

        for (int i=0; i<r.length; i++) {
            r[i]=getValueAsString(i);
        }
        return r;
    }
    
    
    public int getIndex() {
        return validate(sindex.getValue());
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
        setIndex(getIndex()+1);
    }

    
    public int getImageResource() {
        return DEFAULT_IMAGE_RESOURCE;
    }
}
