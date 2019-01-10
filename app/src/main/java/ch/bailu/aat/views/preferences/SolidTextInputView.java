package ch.bailu.aat.views.preferences;

import ch.bailu.aat.preferences.AbsSolidType;

public class SolidTextInputView extends AbsSolidView {
    private final AbsSolidType solid;
    private final int inputType;


    public SolidTextInputView(AbsSolidType s, int input_type) {
        super(s);

        solid = s;
        inputType = input_type;
    }

        @Override
        public void onRequestNewValue() {
            new SolidTextInputDialog(solid, inputType);
        }

}
