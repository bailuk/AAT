package ch.bailu.aat.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTipProvider;
import ch.bailu.aat.util.ui.ToolTipView;
import ch.bailu.aat.util.ui.UiTheme;

public abstract class AbsLabelTextView extends LinearLayout{

    private final TextView value, label;
    private final ToolTipView toolTip;

    public AbsLabelTextView(final Context context, String labelText) {
        this (context, AppTheme.main, labelText);
    }

    public AbsLabelTextView(final Context context, UiTheme theme, String labelText) {
        super(context);
        setOrientation(VERTICAL);

        label = new TextView(context);
        label.setText(labelText);
        theme.header(label);
        addView(label);

        value = new TextView(context);
        theme.content(value);
        addView(value);

        toolTip = new ToolTipView(context);
        addView(toolTip);

        theme.button(this);
        theme.background(this);
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
