package ch.bailu.aat.views;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTipProvider;
import ch.bailu.aat.util.ui.ToolTipView;

public abstract class AbsLabelTextView extends LinearLayout{

    private final TextView value, label;
    private final ToolTipView toolTip;

    public AbsLabelTextView(final Context context, String labelText) {
        super(context);
        setOrientation(VERTICAL);

        label = new TextView(context);
        label.setText(labelText);
        addView(label);
        AppTheme.themify(label);

        value = new TextView(context);
        value.setTextColor(Color.LTGRAY);
        addView(value);

        toolTip = new ToolTipView(context);
        addView(toolTip);

        AppTheme.themify(this);
    }


    public void setText(CharSequence text) {
        value.setText(text);
    }
    public void setToolTip(ToolTipProvider tip) {
        toolTip.setToolTip(tip);
    }

    public void setTextColor(int color) {
        label.setTextColor(color);
    }
}
