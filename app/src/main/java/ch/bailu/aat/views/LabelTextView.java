package ch.bailu.aat.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTipProvider;
import ch.bailu.aat.util.ui.ToolTipView;
import ch.bailu.aat.util.ui.UiTheme;

public class LabelTextView extends LinearLayout{

    private final TextView value, label;
    private final ToolTipView toolTip;

    private final boolean inverse;

    public LabelTextView(final Context context, String labelText, UiTheme theme) {
        this(false, context, labelText, theme);
    }


    public LabelTextView(boolean inverse, final Context context, String labelText,
                         UiTheme theme) {
        super(context);
        setOrientation(VERTICAL);

        this.inverse = inverse;
        label = new TextView(context);
        label.setText(labelText);

        addView(label);

        value = new TextView(context);
        addView(value);

        toolTip = new ToolTipView(context, theme);
        addView(toolTip);

        AppTheme.padding(this);

        themify(theme);
    }

    public void setLabel(CharSequence text) {
        label.setText(text);
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


    public void themify(UiTheme theme) {
        if (inverse) {
            theme.header(value);
            theme.content(label);

        } else {
            theme.header(label);
            theme.content(value);

        }
    }
}
