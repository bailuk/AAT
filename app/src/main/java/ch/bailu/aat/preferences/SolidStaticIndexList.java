package ch.bailu.aat.preferences;

public class SolidStaticIndexList extends SolidIndexList {

    private final String[] labelList;
    
    public SolidStaticIndexList(Storage s, String k, String[] l) {
        super(s,k);
        labelList=l;
    }
    

    @Override
    public int length() {
        return labelList.length;
    }

    @Override
    public String getString() {
        return labelList[getIndex()];
    }

    @Override
    public void setIndex(int i) {
        if (i < labelList.length) super.setIndex(i);
    }

    @Override
    public String[] getStringArray() {
        return labelList;
    }
    
    @Override
    public int getIndex() {
        if (super.getIndex() < length()) {
            return super.getIndex();
        }
        return 0;
    }
}
