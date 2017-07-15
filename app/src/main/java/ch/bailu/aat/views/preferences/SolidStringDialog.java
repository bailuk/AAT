package ch.bailu.aat.views.preferences;


import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidString;

public class SolidStringDialog extends AbsSolidStringDialog {

    private final SolidString solid;

    public SolidStringDialog (SolidString s) {
        super(s);

        solid = s;
    }


    @Override
    protected void buildExtraSelection(Context c, ArrayList<String> sel) {
        sel.add(c.getString(R.string.enter));
    }

    @Override
    protected void onExtraItemClick(int i) {
        new SolidStringInputDialog(solid);
    }
}