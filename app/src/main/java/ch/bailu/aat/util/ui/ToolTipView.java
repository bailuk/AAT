package ch.bailu.aat.util.ui;


import android.content.Context;
import android.widget.TextView;

public class ToolTipView extends TextView {
    public ToolTipView(Context context) {
        super(context);

        AppTheme.main.toolTip(this);
        setVisibility(GONE);
    }

    public void setToolTip(ToolTipProvider tip) {

        String text = tip.getToolTip();

        if (text == null || text.length() == 0) {
            setVisibility(GONE);
        } else {
            setText(text);
            setVisibility(VISIBLE);
        }
    }
}
