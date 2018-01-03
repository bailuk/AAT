package ch.bailu.aat.views.preferences;

import ch.bailu.aat.preferences.SolidFile;

public class SolidDirectoryView extends AbsSolidView {
    protected final SolidFile solid;



    public SolidDirectoryView(SolidFile s) {
        super(s);
        solid = s;

    }


    @Override
    public void onRequestNewValue() {
        new SolidStringDialog(solid);
    }
}
