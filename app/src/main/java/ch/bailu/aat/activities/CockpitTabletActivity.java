package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;

import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.PredictiveTimeDescription;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.dispatcher.TrackerTimerSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.mapsforge.MapViewLinker;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.description.CockpitView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.graph.DistanceSpeedGraphView;
import ch.bailu.aat.views.graph.GraphViewContainer;

public class CockpitTabletActivity extends AbsDispatcher {
    private final static String SOLID_KEY="cockpit_tablet";
    private final static String SOLID_MAP_KEY=SOLID_KEY+"_map";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EditorHelper edit = new EditorHelper(getServiceContext());
        setContentView(createContentView(edit));
        createDispatcher(edit);
    }


    private View createContentView(EditorHelper edit) {
        final MapViewInterface smallMap = MapFactory.DEF(this, SOLID_KEY).split();
        final MapViewInterface bigMap = MapFactory.DEF(this, SOLID_MAP_KEY).map(edit, createButtonBar());
        new MapViewLinker(bigMap, smallMap);


        final PercentageLayout one = new PercentageLayout(this);
        one.setOrientation(AppLayout.getOrientationAlongSmallSide(this));
        one.add(createCockpit(), 60);
        one.add(smallMap.toView(),40);


        final PercentageLayout two = new PercentageLayout(this);
        two.setOrientation(AppLayout.getOrientationAlongLargeSide(this));
        two.add(bigMap.toView(),60);
        two.add(one,40);


        final PercentageLayout three = new PercentageLayout(this);
        three.add(two,80);
        three.add(GraphViewContainer.speedAltitude(this, this, InfoID.TRACKER, SOLID_KEY),20);


        return three;
    }


    private CockpitView createCockpit() {

        CockpitView c = new CockpitView(this);

        c.add(this, new CurrentSpeedDescription(this), InfoID.LOCATION);
        c.add(this, new AltitudeDescription(this), InfoID.LOCATION);
        c.add(this, new PredictiveTimeDescription(this), InfoID.TRACKER_TIMER);
        c.addC(this, new DistanceDescription(this), InfoID.TRACKER);
        c.addC(this, new AverageSpeedDescription(this), InfoID.TRACKER);
        c.add(this, new MaximumSpeedDescription(this), InfoID.TRACKER);

        return c;
    }


    private ControlBar createButtonBar() {
        MainControlBar bar = new MainControlBar(this);

        bar.addActivityCycle(this);
        bar.addSpace();

        if (AppLayout.haveExtraSpaceGps(this)) {
            bar.addGpsState(this);
        }

        bar.addTrackerState(this);
        return bar;
    }


    private void createDispatcher(EditorHelper edit) {
        addSource(new EditorSource(getServiceContext(),edit));
        addSource(new TrackerSource(getServiceContext()));
        addSource(new TrackerTimerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));
    }
}
