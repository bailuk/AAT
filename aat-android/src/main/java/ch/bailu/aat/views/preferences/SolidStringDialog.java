package ch.bailu.aat.views.preferences;


import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.preferences.SolidString;

public class SolidStringDialog extends AbsSolidStringDialog {

    private final SolidString solid;

    private final Context context;

    public SolidStringDialog (Context context, SolidString s) {
        super(context,s);

        solid = s;
        this.context = context;
    }


    @Override
    protected void buildExtraSelection(Context c, ArrayList<String> sel) {
        sel.add(c.getString(R.string.enter));
    }

    @Override
    protected void onExtraItemClick(int i) {
        new SolidTextInputDialog(context, solid, SolidTextInputDialog.TEXT);
    }
}