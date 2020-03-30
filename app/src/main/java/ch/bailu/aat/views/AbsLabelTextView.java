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


    public AbsLabelTextView(final Context context, String labelText, UiTheme theme) {
        this(false, context, labelText, theme);
    }


    public AbsLabelTextView(boolean inverse, final Context context, String labelText,
                             UiTheme theme) {
        super(context);
        setOrientation(VERTICAL);

        label = new TextView(context);
        label.setText(labelText);

        addView(label);

        value = new TextView(context);
        addView(value);

        toolTip = new ToolTipView(context, theme);
        addView(toolTip);

        AppTheme.padding(this);

        if (inverse) {
            theme.header(value);
            theme.content(label);

        } else {
            theme.header(label);
            theme.content(value);

        }

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
