package ch.bailu.aat_awt.views;

import javax.swing.JComboBox;

import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class JSolidComboBox extends JComboBox implements OnPreferencesChanged {
      private final SolidIndexList sindex;


    public JSolidComboBox(SolidIndexList solid) {
        super(solid.getStringArray());
        sindex = solid;
        setSelectedIndex(solid.getIndex());

        addActionListener(actionEvent -> setSelectedIndex(getSelectedIndex()));
        sindex.register(this);
    }

    @Override
    public void onPreferencesChanged(StorageInterface storage, String key) {
        if (sindex.hasKey(key)) {
            setSelectedIndex(sindex.getIndex());
        }
    }

    @Override
    public void setSelectedIndex(int index) {
        if (getSelectedIndex() != index) {
            super.setSelectedIndex(index);
        }

        if (sindex.getIndex() != index) {
            sindex.setIndex(index);
        }
    }
}
