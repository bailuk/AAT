package ch.bailu.aat.views.preferences;

import ch.bailu.aat.preferences.SolidIndexList;

public class SolidIndexListView extends AbsSolidView {
    private final SolidIndexList solid;

    public SolidIndexListView(SolidIndexList s) {
        super(s);
        solid = s;
    }

    @Override
    public void onRequestNewValue() {
        if (solid.length()<3) {
            solid.cycle();
        } else {
            new SolidIndexListDialog(solid);
        }
    }
}
