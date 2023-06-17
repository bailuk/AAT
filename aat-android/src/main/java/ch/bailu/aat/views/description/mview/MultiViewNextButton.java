package ch.bailu.aat.views.description.mview;

import android.view.KeyEvent;

import javax.annotation.Nonnull;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsHardwareButtons;
import ch.bailu.aat.util.ui.theme.UiTheme;
import ch.bailu.aat.views.ImageButtonViewGroup;

public class MultiViewNextButton extends ImageButtonViewGroup implements AbsHardwareButtons.OnHardwareButtonPressed {
    private final MultiView mv;


    public MultiViewNextButton(MultiView mv, UiTheme theme) {
        super(mv.getContext(), R.drawable.go_next_inverse);
        this.mv = mv;
        setOnClickListener(v -> mv.setNext());
        theme.button(this);
    }

    @Override
    public boolean onHardwareButtonPressed(int code, @Nonnull AbsHardwareButtons.EventType type) {
        if (code == KeyEvent.KEYCODE_SEARCH) {
            if (type == AbsHardwareButtons.EventType.DOWN) mv.setNext();
            return true;
        }
        return false;
    }
}
