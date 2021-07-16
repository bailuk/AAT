package ch.bailu.aat.util.ui;


import android.content.Context;

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
