package ch.bailu.aat_awt.views;

import javax.swing.JButton;

import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class JSolidIndexButton extends JButton implements OnPreferencesChanged {
    private final int TEXT_LEN = 6;
    private final SolidIndexList sindex;

    public JSolidIndexButton(SolidIndexList solid) {
        sindex = solid;
        setText(sindex.getValueAsString());
        addActionListener(actionEvent -> sindex.cycle());
        sindex.register(this);
    }

    @Override
    public void onPreferencesChanged(StorageInterface storage, String key) {
        if (sindex.hasKey(key)) {
            setText(sindex.getValueAsString());
        }
    }

    @Override
    public void setText(String text) {
        if (text.length() < TEXT_LEN) {
            super.setText(text);
            setToolTipText(sindex.getToolTip());
        } else {
            setToolTipText(text);
            super.setText(text.substring(0, TEXT_LEN));
        }
    }
}
