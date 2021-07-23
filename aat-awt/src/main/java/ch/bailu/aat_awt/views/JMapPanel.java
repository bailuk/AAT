package ch.bailu.aat_awt.views;

import java.awt.BorderLayout;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import ch.bailu.aat_awt.map.AwtCustomMapView;
import ch.bailu.aat_awt.preferences.AwtSolidLocationProvider;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer;
import ch.bailu.aat_lib.map.layer.grid.GridDynLayer;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.location.CurrentLocationLayer;
import ch.bailu.aat_lib.preferences.map.SolidMapGrid;
import ch.bailu.aat_lib.preferences.map.SolidPositionLock;
import ch.bailu.aat_lib.service.ServicesInterface;

public class JMapPanel extends JPanel {
    final AwtCustomMapView map;
    final JPanel positionPane = new JPanel();

    private final JButton
            showMap = new JButton("Map"),
            plus = new JButton("+"),
            minus = new JButton("-");


    private final JSolidToggleButton center;
    private final JSolidIndexButton  grid;
    private final JSolidIndexButton  provider;

    public JMapPanel(List<File> mapFiles, ServicesInterface services, StorageInterface storage, DispatcherInterface dispatcher) {

        setLayout(new BorderLayout());

        map = new AwtCustomMapView(storage, mapFiles, dispatcher);
        String key = map.getMContext().getSolidKey();

        center = new JSolidToggleButton(new SolidPositionLock(storage, key));
        grid = new JSolidIndexButton(new SolidMapGrid(storage, key));
        provider = new JSolidIndexButton(new AwtSolidLocationProvider(storage));

        showMap.addActionListener(itemEvent -> map.showMap());
        plus.addActionListener(itemEvent -> map.zoomIn());
        minus.addActionListener(itemEvent -> map.zoomOut());

        positionPane.add(Box.createHorizontalGlue());
        positionPane.add(center);
        positionPane.add(showMap);
        positionPane.add(grid);
        positionPane.add(provider);
        positionPane.add(Box.createHorizontalGlue());
        positionPane.add(minus);
        positionPane.add(plus);
        positionPane.add(Box.createHorizontalGlue());

        add(map, BorderLayout.CENTER);
        add(positionPane, BorderLayout.PAGE_END);

        map.add(new CurrentLocationLayer(map.getMContext(), dispatcher));
        map.add(new GridDynLayer(services, storage, map.getMContext()));
        map.add(new GpxDynLayer(storage, map.getMContext(), services, dispatcher, InfoID.TRACKER));

    }

}
