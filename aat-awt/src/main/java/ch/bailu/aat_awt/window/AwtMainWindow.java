package ch.bailu.aat_awt.window;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import ch.bailu.aat_awt.app.App;
import ch.bailu.aat_awt.preferences.AwtStorage;
import ch.bailu.aat_awt.preferences.SolidMainWindow;
import ch.bailu.aat_awt.views.JCockpitPanel;
import ch.bailu.aat_awt.views.JMapPanel;
import ch.bailu.aat_awt.views.JNumberView;
import ch.bailu.aat_awt.views.JPreferencesPane;
import ch.bailu.aat_lib.app.AppConfig;
import ch.bailu.aat_lib.description.AltitudeDescription;
import ch.bailu.aat_lib.description.FF;
import ch.bailu.aat_lib.description.GpsStateDescription;
import ch.bailu.aat_lib.description.LatitudeDescription;
import ch.bailu.aat_lib.description.LongitudeDescription;
import ch.bailu.aat_lib.description.TrackerStateDescription;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.dispatcher.Dispatcher;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.service.ServicesInterface;

public class AwtMainWindow implements OnContentUpdatedInterface {
    final JFrame frame;
    final JMapPanel map;

    private final JLabel
            locationStatus = new JLabel(),
            trackerStatus = new JLabel(),
            errorStatus = new JLabel(),
            infoStatus = new JLabel();

    private final JCockpitPanel cockpit;
    private final JPanel
            buttonPane = new JPanel(),
            statusPane = new JPanel();

    private final JPreferencesPane preferences;

    private final JNumberView
            gpsButton = new JNumberView(new GpsStateDescription()),
            trackerButton = new JNumberView(new TrackerStateDescription());

    private final JTabbedPane tabbedPane = new JTabbedPane();


    public AwtMainWindow(List<File> mapFiles, ServicesInterface services, Broadcaster broadcaster, Dispatcher dispatcher) {

        preferences = new JPreferencesPane(new AwtStorage());
        cockpit = new JCockpitPanel(dispatcher);

        frame = new JFrame(AppConfig.getInstance().getLongName());
        frame.getContentPane().setLayout(new BorderLayout());

        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        map = new JMapPanel(mapFiles, services, new AwtStorage(), dispatcher);
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(gpsButton);
        buttonPane.add(trackerButton);


        Container pane = frame.getContentPane();
        pane.add(buttonPane, BorderLayout.PAGE_START);
        tabbedPane.addTab("Map",map);
        tabbedPane.addTab("Cockpit",cockpit);
        tabbedPane.addTab("Preferences",preferences);

        pane.add(tabbedPane, BorderLayout.CENTER);

        statusPane.setLayout(new BoxLayout(statusPane, BoxLayout.PAGE_AXIS));
        statusPane.add(locationStatus);
        statusPane.add(trackerStatus);
        statusPane.add(errorStatus);
        statusPane.add(infoStatus);
        pane.add(statusPane, BorderLayout.PAGE_END);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {

            }
        });

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                doQuit();
            }
        });


        try {
            Image icon = ImageIO.read(getClass().getResourceAsStream("/images/icon.png"));
            frame.setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.pack();
        frame.setVisible(true);
        new SolidMainWindow(new AwtStorage()).load(tabbedPane);
        new SolidMainWindow(new AwtStorage()).load(frame);

        broadcaster.register(objects -> {
            String tag = (String) objects[0];
            String msg = (String) objects[1];
            errorStatus.setText(tag + ": " + msg);
        }, AppBroadcaster.LOG_ERROR);

        broadcaster.register(objects -> {
            String tag = (String) objects[0];
            String msg = (String) objects[1];
            infoStatus.setText(tag + ": " + msg);
        }, AppBroadcaster.LOG_INFO);



    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        LatitudeDescription la = new LatitudeDescription();
        LongitudeDescription lo = new LongitudeDescription();
        AltitudeDescription altitude = new AltitudeDescription(new AwtStorage());
        GpsStateDescription gps = new GpsStateDescription();
        TrackerStateDescription tracker = new TrackerStateDescription();

        la.onContentUpdated(iid, info);
        lo.onContentUpdated(iid, info);
        altitude.onContentUpdated(iid, info);

        String time = FF.f().LOCAL_TIME.format(info.getTimeStamp());

        if (iid == InfoID.LOCATION) {
            gps.onContentUpdated(iid, info);
            gpsButton.onContentUpdated(iid, info);

            locationStatus.setText(time + " " + gps.getLabel() + " " + gps.getValue() + " " + la.getValue() + ", " + lo.getValue() +", " + altitude.getValue());

        } else if (iid == InfoID.TRACKER) {
            tracker.onContentUpdated(iid, info);
            trackerButton.onContentUpdated(iid, info);
            trackerStatus.setText(time + ": "+ tracker.getLabel() + " " + tracker.getValue() + " " + la.getValue() + ", " + lo.getValue() +", " + altitude.getValue());
        }
    }


    private void doQuit() {
        new SolidMainWindow(new AwtStorage()).save(tabbedPane);
        new SolidMainWindow(new AwtStorage()).save(frame);

        frame.dispose();
        App.exit(0);
    }

}