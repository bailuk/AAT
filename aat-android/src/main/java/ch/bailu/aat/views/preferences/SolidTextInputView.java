package ch.bailu.aat.views.preferences;

import android.content.Context;

import ch.bailu.aat.util.ui.theme.UiTheme;
import ch.bailu.aat_lib.preferences.AbsSolidType;

public class SolidTextInputView extends AbsSolidView {
    private final AbsSolidType solid;
    private final int inputType;


    public SolidTextInputView(Context c, AbsSolidType s, int input_type, UiTheme theme) {
        super(c,s, theme);

        solid = s;
        inputType = input_type;
    }

        @Override
        public void onRequestNewValue() {
            new SolidTextInputDialog(getContext(),solid, inputType);
        }

}
