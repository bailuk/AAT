package ch.bailu.aat.util.ui;

import android.os.Build;
import android.view.View;

public class ToolTip {
    public static void set(View view, Integer resID) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.setTooltipText(new ToolTipRes(view.getContext(), resID).getToolTip());
        } else {
            new ToolTipLongClick(view, new ToolTipRes(view.getContext(), resID));
        }
    }
}
