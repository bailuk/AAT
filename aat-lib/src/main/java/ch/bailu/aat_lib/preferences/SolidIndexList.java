package ch.bailu.aat_lib.preferences;

public abstract class SolidIndexList extends AbsSolidType {

    private final SolidInteger sindex;


    public SolidIndexList(StorageInterface s, String k) {
        sindex = new SolidInteger(s,k);
    }


    public abstract int length();
    protected abstract String getValueAsString(int i);

    @Override
    public String getValueAsString() {
        return getValueAsString(getIndex());
    }
    public void setIndex(int i) {
        sindex.setValue(validate(i));
    }

    @Override
    public void setValueFromString(String s) {}

    protected int validate(int index) {
        if (index < 0) index = length()-1;
        else if (index >= length()) index=0;
        return index;
    }

    public String[] getStringArray() {
        String[] r = new String[length()];

        for (int index = 0; index < r.length; index++) {
            r[index] = getValueAsString(index);
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
    public StorageInterface getStorage() {
        return sindex.getStorage();
    }

    public void cycle() {
        setIndex(getIndex() + 1);
    }
}
