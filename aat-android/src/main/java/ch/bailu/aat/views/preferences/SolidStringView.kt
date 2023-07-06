package ch.bailu.aat.views.preferences;

import android.content.Context;

import ch.bailu.aat.util.ui.theme.UiTheme;
import ch.bailu.aat.views.preferences.dialog.SolidStringDialog;
import ch.bailu.aat_lib.preferences.SolidString;

public class SolidStringView extends AbsSolidView {
    private final SolidString solid;

    public SolidStringView(Context c, SolidString s, UiTheme theme) {
        super(c,s, theme);
        solid = s;
    }

    @Override
    public void onRequestNewValue() {
        new SolidStringDialog(getContext(),solid);
    }
}
