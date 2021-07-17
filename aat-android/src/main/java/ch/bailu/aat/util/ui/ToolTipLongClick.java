package ch.bailu.aat.util.ui;


import android.view.View;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.util.ui.ToolTipProvider;

public class ToolTipLongClick implements View.OnLongClickListener{

    private final ToolTipProvider tip;

    public ToolTipLongClick(View v, ToolTipProvider t) {
        tip = t;

        v.setLongClickable(true);
        v.setOnLongClickListener(this);

    }

    @Override
    public boolean onLongClick(View view) {
        String text = tip.getToolTip();

        if (text != null && text.length()>0) {
            AppLog.i(view.getContext(), text);
            return true;
        }
        return false;
    }
}
