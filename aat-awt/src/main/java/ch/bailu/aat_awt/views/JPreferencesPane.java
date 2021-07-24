package ch.bailu.aat_awt.views;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.bailu.aat_awt.preferences.AwtSolidLocationProvider;
import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class JPreferencesPane extends JPanel {



    public JPreferencesPane(StorageInterface storage) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));

        addItem(box, new AwtSolidLocationProvider(storage));

        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(box);
    }


    private void addItem(JPanel box, SolidIndexList sindex) {
        addItem(box, new JLabel(sindex.getLabel()));
        addItem(box, new JSolidComboBox(sindex));
        addItem(box, new JLabel(sindex.getToolTip()));
    }
    private void addItem(JPanel box, JComponent component) {
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(component);
    }
}
