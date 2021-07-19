package ch.bailu.aat_awt.views;

import java.util.ArrayList;

import javax.swing.JPanel;

import ch.bailu.aat_awt.preferences.AwtStorage;
import ch.bailu.aat_lib.description.AltitudeDescription;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;

public class JCockpitPanel extends JPanel implements OnContentUpdatedInterface {
    private final ArrayList<JNumberView> items = new ArrayList<>(20);


    public JCockpitPanel() {
        addItem(new AltitudeDescription(new AwtStorage()));
    }

    private void addItem(ContentDescription description) {
        JNumberView item = new JNumberView(description);
        items.add(item);
        add(item);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        for(JNumberView item : items) {
            item.onContentUpdated(iid, info);
        }
    }
}
