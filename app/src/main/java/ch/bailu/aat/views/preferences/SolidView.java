package ch.bailu.aat.views.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.preferences.SolidType;

public abstract class SolidView extends LinearLayout
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final TextView value;
    private final SolidType solid;

    public SolidView(final Context context, final SolidType s) {
        super(context);
        setOrientation(VERTICAL);

        solid = s;

        final TextView label = new TextView(context);
        label.setText(solid.getLabel());
        addView(label);
        AppTheme.themify(label);

        value = new TextView(context);
        value.setText(solid.getValueAsString());
        value.setTextColor(Color.LTGRAY);

        addView(value);


        AppTheme.themify(this);


        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestNewValue();
            }
        });
    }

    public abstract void onRequestNewValue();


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        solid.register(this);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (solid.hasKey(key)) {
            value.setText(solid.getValueAsString());
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        solid.unregister(this);
    }
}

