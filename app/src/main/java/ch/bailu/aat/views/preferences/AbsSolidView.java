package ch.bailu.aat.views.preferences;

import android.content.SharedPreferences;
import android.view.View;

import ch.bailu.aat.preferences.AbsSolidType;
import ch.bailu.aat.views.AbsLabelTextView;

public abstract class AbsSolidView extends AbsLabelTextView
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final AbsSolidType solid;

    public AbsSolidView(final AbsSolidType s) {
        super(s.getContext(), s.getLabel());

        solid = s;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestNewValue();
            }
        });
    }

    public abstract void onRequestNewValue();


    public void setText() {
        setText("["+solid.getValueAsString()+"]");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        solid.register(this);
        setText();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (solid.hasKey(key)) {
            setText();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        solid.unregister(this);
    }
}

