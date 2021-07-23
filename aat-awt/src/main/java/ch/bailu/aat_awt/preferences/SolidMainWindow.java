package ch.bailu.aat_awt.preferences;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import ch.bailu.aat_lib.preferences.SolidInteger;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class SolidMainWindow {
    private final static String KEY = SolidMainWindow.class.getSimpleName();
    private final SolidInteger x;
    private final SolidInteger y;
    private final SolidInteger width;
    private final SolidInteger height;
    private final SolidInteger tab;

    public SolidMainWindow(StorageInterface storage) {
        x = new SolidInteger(storage, KEY + "_x");
        y = new SolidInteger(storage, KEY + "_y");
        width = new SolidInteger(storage, KEY + "_width");
        height = new SolidInteger(storage, KEY + "_height");
        tab = new SolidInteger(storage, KEY + "_tab");
    }


    public void load(JFrame frame) {
        if (width.getValue() == 0 || height.getValue() == 0) {
            // FIXME: frame does not receive resize event from initial window placement on Mobian phosh
            setSize(frame,370, 675);

        } else {
            setSize(frame, width.getValue(), height.getValue());
        }

        frame.setLocation(x.getValue(), y.getValue());
    }

    private void setSize(JFrame frame, int w, int h) {
        frame.setPreferredSize(new Dimension(w, h));
        frame.setSize(w, h);
    }


    public void save(JFrame frame) {
        width.setValue(frame.getWidth());
        height.setValue(frame.getHeight());
        x.setValue(frame.getX());
        y.setValue(frame.getY());
    }


    public void load(JTabbedPane t) {
        t.setSelectedIndex(tab.getValue());
    }


    public void save(JTabbedPane t) {
        tab.setValue(tab.getValue());
    }
}
