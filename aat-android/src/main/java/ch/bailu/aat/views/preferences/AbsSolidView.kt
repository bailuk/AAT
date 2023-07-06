package ch.bailu.aat.views.preferences;

import android.content.Context;

import javax.annotation.Nonnull;

import ch.bailu.aat.util.ui.theme.UiTheme;
import ch.bailu.aat.views.LabelTextView;
import ch.bailu.aat_lib.preferences.AbsSolidType;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;

public abstract class AbsSolidView extends LabelTextView
        implements OnPreferencesChanged {

    private final AbsSolidType solid;

    public AbsSolidView(Context context, final AbsSolidType s, UiTheme theme) {
        super(context, s.getLabel(), theme);
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
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {
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
