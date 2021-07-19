package ch.bailu.aat_awt.views;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import ch.bailu.aat_awt.preferences.AwtStorage;
import ch.bailu.aat_lib.description.AltitudeDescription;
import ch.bailu.aat_lib.description.AveragePaceDescriptionAP;
import ch.bailu.aat_lib.description.AverageSpeedDescription;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.description.CurrentPaceDescription;
import ch.bailu.aat_lib.description.CurrentSpeedDescription;
import ch.bailu.aat_lib.description.DistanceApDescription;
import ch.bailu.aat_lib.description.MaximumSpeedDescription;
import ch.bailu.aat_lib.description.TimeDescription;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class JCockpitPanel extends JPanel implements OnContentUpdatedInterface {
    private final ArrayList<JNumberView> items = new ArrayList<>(20);

    private final StorageInterface storage = new AwtStorage();

    public JCockpitPanel() {
        addItem(new CurrentSpeedDescription(storage));
        addItem(new AltitudeDescription(storage));
        addItem(new TimeDescription());
        addItem(new DistanceApDescription(storage));
        addItem(new AverageSpeedDescription(storage));
        addItem(new MaximumSpeedDescription(storage));
        addItem(new CurrentPaceDescription(storage));
        addItem(new AveragePaceDescriptionAP(storage));
    }

    private void addItem(ContentDescription description) {
        JNumberView item = new JNumberView(description);
        item.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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
