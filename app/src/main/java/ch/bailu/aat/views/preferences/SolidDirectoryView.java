package ch.bailu.aat.views.preferences;

import android.app.Activity;

import ch.bailu.aat.preferences.SolidDirectory;

public class SolidDirectoryView extends AbsSolidView {
    private final SolidDirectory sdirectory;

    private final Activity acontext;
    public SolidDirectoryView(Activity context, SolidDirectory s) {
        super(context, s);

        acontext = context;
        sdirectory = s;
    }

    @Override
    public void onRequestNewValue() {
        new SolidDirectoryDialog(acontext, sdirectory);
    }
}
