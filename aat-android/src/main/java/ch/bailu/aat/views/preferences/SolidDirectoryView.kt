package ch.bailu.aat.views.preferences;

import android.content.Context;

import ch.bailu.aat.util.ui.theme.UiTheme;
import ch.bailu.aat.views.preferences.dialog.SolidStringDialog;
import ch.bailu.aat_lib.preferences.SolidFile;

public class SolidDirectoryView extends AbsSolidView {
    protected final SolidFile solid;
    private final Context context;

    public SolidDirectoryView(Context context, SolidFile s, UiTheme theme) {
        super(context,s, theme);
        solid = s;
        this.context = context;
    }

    @Override
    public void onRequestNewValue() {
        new SolidStringDialog(context, solid);
    }
}
