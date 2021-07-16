package ch.bailu.aat.views.preferences;

import ch.bailu.aat.preferences.SolidString;
import ch.bailu.aat.util.ui.UiTheme;

public class SolidStringView extends AbsSolidView {
    private final SolidString solid;


    public SolidStringView(SolidString s, UiTheme theme) {
        super(s, theme);

        solid = s;
    }

    @Override
    public void onRequestNewValue() {
        new SolidStringDialog(solid);
    }
}

