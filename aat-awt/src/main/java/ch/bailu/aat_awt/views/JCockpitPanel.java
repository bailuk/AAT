package ch.bailu.aat_awt.views;

import java.awt.FlowLayout;

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
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class JCockpitPanel extends JPanel {

    private final static int BORDER=6;

    private final StorageInterface storage = new AwtStorage();

    public JCockpitPanel(DispatcherInterface dispatcher) {
        setLayout(new FlowLayout(FlowLayout.LEADING));
        dispatcher.addTarget(addItem(new CurrentSpeedDescription(storage)), InfoID.LOCATION);
        dispatcher.addTarget(addItem(new AltitudeDescription(storage)), InfoID.LOCATION);
        dispatcher.addTarget(addItem(new TimeDescription()), InfoID.TRACKER);
        dispatcher.addTarget(addItem(new DistanceApDescription(storage)), InfoID.TRACKER);
        dispatcher.addTarget(addItem(new AverageSpeedDescription(storage)), InfoID.TRACKER);
        dispatcher.addTarget(addItem(new MaximumSpeedDescription(storage)), InfoID.TRACKER);
        dispatcher.addTarget(addItem(new CurrentPaceDescription(storage)), InfoID.TRACKER);
        dispatcher.addTarget(addItem(new AveragePaceDescriptionAP(storage)), InfoID.TRACKER);
    }

    private JNumberView addItem(ContentDescription description) {
        JNumberView item = new JNumberView(description);

        item.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
        add(item);
        return item;
    }
}
