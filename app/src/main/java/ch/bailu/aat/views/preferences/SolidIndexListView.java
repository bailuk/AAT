package ch.bailu.aat.views.preferences;

import android.content.Context;

import ch.bailu.aat.preferences.IndexListDialog;
import ch.bailu.aat.preferences.SolidIndexList;

public class SolidIndexListView extends SolidView {
    private final SolidIndexList solid;

    public SolidIndexListView(Context context, SolidIndexList s) {
        super(context, s);
        solid = s;
    }

    @Override
    public void onRequestNewValue() {
        if (solid.length()<3) {
            solid.cycle();
        } else {
            new IndexListDialog(getContext(), solid);
        }
    }
}
