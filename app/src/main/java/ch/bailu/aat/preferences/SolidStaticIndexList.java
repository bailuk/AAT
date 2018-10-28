package ch.bailu.aat.preferences;

import android.content.Context;

public class SolidStaticIndexList extends SolidIndexList {

    private final String[] labelList;
    
    public SolidStaticIndexList(Context c, String k, String[] l) {
        super(c,k);
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
