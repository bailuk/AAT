package ch.bailu.aat.views.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import ch.bailu.aat.preferences.SolidType;
import ch.bailu.aat.views.AbsLabelTextView;

public abstract class AbsSolidView extends AbsLabelTextView
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final SolidType solid;

    public AbsSolidView(final SolidType s) {
        super(s.getContext(), s.getLabel());

        solid = s;

        setText(solid.getValueAsString());
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
            setText(solid.getValueAsString());
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        solid.unregister(this);
    }
}

