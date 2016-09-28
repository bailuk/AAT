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
    public String getValueAsString(int i) {
        return labelList[i];
    }


    @Override
    public String[] getStringArray() {
        return labelList;
    }
    

}
