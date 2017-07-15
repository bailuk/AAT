package ch.bailu.aat.views.preferences;

import android.app.Activity;

import ch.bailu.aat.preferences.SolidFile;

public class SolidDirectoryView extends AbsSolidView {
    private final SolidFile solid;
    private final Activity acontext;


    public SolidDirectoryView(Activity ac, SolidFile s) {
        super(s);
        acontext = ac;
        solid = s;
    }

    @Override
    public void onRequestNewValue() {
        new SolidDirectoryDialog(acontext, solid);
    }
}
