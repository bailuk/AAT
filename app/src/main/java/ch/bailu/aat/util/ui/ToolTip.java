package ch.bailu.aat.util.ui;

import android.view.View;
import android.view.View.OnLongClickListener;

public class ToolTip {
    public static void set(View view, Integer resID) {
        new ToolTipLongClick(view, new ToolTipRes(view.getContext(), resID));
    }
}
