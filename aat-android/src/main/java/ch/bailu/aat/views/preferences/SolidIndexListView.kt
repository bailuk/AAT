package ch.bailu.aat.views.preferences;

import android.content.Context;

import ch.bailu.aat.util.ui.theme.UiTheme;
import ch.bailu.aat.views.preferences.dialog.SolidIndexListDialog;
import ch.bailu.aat_lib.preferences.SolidIndexList;

public class SolidIndexListView extends AbsSolidView {
    private final SolidIndexList solid;

    public SolidIndexListView(Context c,  SolidIndexList s, UiTheme theme) {
        super(c, s, theme);
        solid = s;
    }

    @Override
    public void onRequestNewValue() {
        if (solid.length()<3) {
            solid.cycle();
        } else {
            new SolidIndexListDialog(getContext(),solid);
        }
    }
}
