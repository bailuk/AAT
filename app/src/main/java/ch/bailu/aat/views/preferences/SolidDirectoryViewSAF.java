package ch.bailu.aat.views.preferences;

import android.app.Activity;

import ch.bailu.aat.preferences.SolidFile;

public class SolidDirectoryViewSAF extends SolidDirectoryView {
    private final Activity acontext;

    public SolidDirectoryViewSAF(Activity ac, SolidFile s) {
        super(s);
        acontext = ac;

    }

    @Override
    public void onRequestNewValue() {
        new SolidDirectoryDialog(acontext, solid);
    }
}
