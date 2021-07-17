package ch.bailu.aat.util.ui;


import android.content.Context;

import ch.bailu.aat_lib.util.ui.ToolTipProvider;

public class ToolTipRes implements ToolTipProvider {
    private final int res;
    private final Context context;

    public ToolTipRes(Context c, int r) {
        res = r;
        context = c;
    }

    @Override
    public String getToolTip() {
        return context.getResources().getString(res);
    }
}
