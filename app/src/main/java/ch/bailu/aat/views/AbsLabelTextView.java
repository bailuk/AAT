package ch.bailu.aat.views;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.helpers.AppTheme;

public abstract class AbsLabelTextView extends LinearLayout{

    private final TextView value;

    public AbsLabelTextView(final Context context, String labelText) {
        super(context);
        setOrientation(VERTICAL);

        final TextView label = new TextView(context);
        label.setText(labelText);
        addView(label);
        AppTheme.themify(label);

        value = new TextView(context);
        value.setTextColor(Color.LTGRAY);

        addView(value);


        AppTheme.themify(this);
    }


    public void setText(CharSequence text) {
        value.setText(text);
    }
}
