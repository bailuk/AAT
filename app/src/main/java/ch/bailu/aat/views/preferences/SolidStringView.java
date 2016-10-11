package ch.bailu.aat.views.preferences;

import ch.bailu.aat.preferences.SolidString;

public class SolidStringView extends AbsSolidView {
    private final SolidString solid;


    public SolidStringView(SolidString s) {
        super(s);

        solid = s;
    }

    @Override
    public void onRequestNewValue() {
        new SolidStringDialog(solid);
    }
}
