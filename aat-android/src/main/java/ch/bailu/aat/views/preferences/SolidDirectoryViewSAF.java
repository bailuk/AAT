package ch.bailu.aat.views.preferences;

import android.app.Activity;

import ch.bailu.aat.util.ui.theme.UiTheme;
import ch.bailu.aat_lib.preferences.SolidFile;

public class SolidDirectoryViewSAF extends SolidDirectoryView {
    private final Activity acontext;

    public SolidDirectoryViewSAF(Activity ac, SolidFile s, UiTheme theme) {
        super(ac,s, theme);
        acontext = ac;

    }

    @Override
    public void onRequestNewValue() {
        new SolidDirectoryDialog(acontext, solid);
    }
}
