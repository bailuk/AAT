package ch.bailu.aat.preferences;

import android.content.Context;

public class SolidOverlayFileList extends SolidCheckList {
    public static final int MAX_OVERLAYS=4;

    private final SolidOverlayFile[] list = new SolidOverlayFile[MAX_OVERLAYS];

    public SolidOverlayFileList(Context context) {
        for (int i=0; i<list.length; i++)
            list[i] = new SolidOverlayFile(context,i);
    }


    public SolidOverlayFile get(int i) {
        i=Math.min(MAX_OVERLAYS, i);
        i=Math.max(0, i);

        return list[i];
    }

    @Override
    public CharSequence[] getStringArray() {
        String[] array = new String[MAX_OVERLAYS];
        for (int i=0; i<list.length; i++)
            array[i] = list[i].getName();
        return array;
    }


    @Override
    public boolean[] getEnabledArray() {
        boolean[] array = new boolean[MAX_OVERLAYS];
        for (int i=0; i<list.length; i++)
            array[i] = list[i].isEnabled();
        return array;
    }


    @Override
    public void setEnabled(int i, boolean isChecked) {
        get(i).setEnabled(isChecked);
    }


    @Override
    public String getKey() {
        return list[0].getKey();
    }


    @Override
    public Storage getStorage() {
        return list[0].getStorage();
    }
    
    @Override
    public boolean hasKey(String s) {
        for (SolidOverlayFile aList : list) if (aList.hasKey(s)) return true;
        return false;
    }   
    
}
