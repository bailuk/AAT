package ch.bailu.aat.views.preferences;

import android.content.Context;

import ch.bailu.aat.preferences.SolidInteger;

public class SolidIntegerView extends AbsSolidView {
    private final SolidInteger sinteger;

    public SolidIntegerView(SolidInteger i) {
        super(i);

        sinteger=i;
    }

        @Override
        public void onRequestNewValue() {
            new SolidStringInputDialog(sinteger);
        }

}
