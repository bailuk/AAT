package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;

import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.CadenceDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.HeartRateDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.PredictiveTimeDescription;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.EditorSourceInterface;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.SensorSource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.dispatcher.TrackerTimerSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.mapsforge.MapViewLinker;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.CockpitView;
import ch.bailu.aat.views.graph.GraphViewFactory;

public class CockpitTabletActivity extends AbsKeepScreenOnActivity {
    private final static String SOLID_KEY="cockpit_tablet";
    private final static String SOLID_MAP_KEY=SOLID_KEY+"_map";

    private final UiTheme theme  = AppTheme.cockpit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EditorSource edit = new EditorSource(getServiceContext());

        setContentView(createContentView(edit));
        createDispatcher(edit);


    }

    private View createContentView(EditorSourceInterface edit) {
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
        three.add(GraphViewFactory.all(this, this, theme, InfoID.TRACKER),20);


        final ContentView contentView = new ContentView(this, theme);
        contentView.add(getErrorView());
        contentView.add(three);

        return three;
    }


    private CockpitView createCockpit() {

        CockpitView c = new CockpitView(this, theme);

        c.add(this, new CurrentSpeedDescription(this),
                InfoID.SPEED_SENSOR, InfoID.LOCATION);
        c.addC(this, new AverageSpeedDescription(this), InfoID.TRACKER);
        c.add(this, new CadenceDescription(this), InfoID.CADENCE_SENSOR);
        c.add(this, new PredictiveTimeDescription(this), InfoID.TRACKER_TIMER);
        c.addC(this, new DistanceDescription(this), InfoID.TRACKER);
        c.add(this, new AltitudeDescription(this), InfoID.LOCATION);

        c.add(this, new MaximumSpeedDescription(this), InfoID.TRACKER);
        c.add(this, new HeartRateDescription(this), InfoID.HEART_RATE_SENSOR);

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


    private void createDispatcher(EditorSource edit) {
        addSource(edit);
        addSource(new TrackerSource(getServiceContext()));
        addSource(new TrackerTimerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));
        addSource(new SensorSource(getServiceContext(), InfoID.HEART_RATE_SENSOR));
        addSource(new SensorSource(getServiceContext(), InfoID.CADENCE_SENSOR));
        addSource(new SensorSource(getServiceContext(), InfoID.SPEED_SENSOR));
    }
}
