package ch.bailu.aat.views.preferences;

import android.content.SharedPreferences;

import ch.bailu.aat.preferences.AbsSolidType;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.AbsLabelTextView;

public abstract class AbsSolidView extends AbsLabelTextView
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final AbsSolidType solid;

    public AbsSolidView(final AbsSolidType s, UiTheme theme) {
        super(s.getContext(), s.getLabel(), theme);

        solid = s;

        theme.button(this);
        setOnClickListener(v -> onRequestNewValue());
    }

    public abstract void onRequestNewValue();


    public void setText() {
        setText("["+solid.toString()+"]");

        setToolTip(solid);
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

