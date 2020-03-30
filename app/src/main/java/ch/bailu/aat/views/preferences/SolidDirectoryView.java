package ch.bailu.aat.views.preferences;

import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.util.ui.UiTheme;

public class SolidDirectoryView extends AbsSolidView {
    protected final SolidFile solid;



    public SolidDirectoryView(SolidFile s, UiTheme theme) {
        super(s, theme);
        solid = s;
    }


    @Override
    public void onRequestNewValue() {
        new SolidStringDialog(solid);
    }
}
