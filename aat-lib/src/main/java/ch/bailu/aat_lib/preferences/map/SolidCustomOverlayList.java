package ch.bailu.aat_lib.preferences.map;

import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.SolidCheckList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.foc.FocFactory;

public class SolidCustomOverlayList extends SolidCheckList {
    public static final int MAX_OVERLAYS=4;

    private final SolidCustomOverlay[] list = new SolidCustomOverlay[MAX_OVERLAYS];

    public SolidCustomOverlayList(StorageInterface storage, FocFactory focFactory) {
        for (int i = 0; i<list.length; i++) {
            list[i] = new SolidCustomOverlay(storage, focFactory, InfoID.OVERLAY + i);
        }
    }

    public SolidCustomOverlay get(int i) {
        i=Math.min(MAX_OVERLAYS - 1, i);
        i=Math.max(0, i);

        return list[i];
    }

    @Override
    public String[] getStringArray() {
        String[] array = new String[list.length];
        for (int i=0; i < list.length; i++) {
            array[i] = list[i].getLabel();
        }
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
    public StorageInterface getStorage() {
        return list[0].getStorage();
    }

    @Override
    public boolean hasKey(String s) {
        for (SolidOverlayInterface aList : list) if (aList.hasKey(s)) return true;
        return false;
    }

    @Override
    public String getLabel() {
        return Res.str().file_overlay();
    }

    @Override
    public String getIconResource() {return "view_paged_inverse";}
}
