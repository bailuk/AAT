package ch.bailu.aat_awt.views;

import javax.swing.JToggleButton;

import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class JSolidToggleButton extends JToggleButton implements OnPreferencesChanged {

    private final SolidBoolean sboolean;

    public JSolidToggleButton(SolidBoolean solid) {
        sboolean = solid;

        set(sboolean.getValue());
        setText(sboolean.getLabel());
        setToolTipText(sboolean.getToolTip());

        addActionListener(actionEvent -> set(isSelected()));
        sboolean.register(this);
    }

    @Override
    public void onPreferencesChanged(StorageInterface storage, String key) {
        if (sboolean.hasKey(key)) {
            set(sboolean.getValue());
        }
    }

    private void set(boolean state) {
        if (state != isSelected()) {
            setSelected(state);
        }

        if (state != sboolean.getValue()) {
            sboolean.setValue(state);
        }
    }
}
