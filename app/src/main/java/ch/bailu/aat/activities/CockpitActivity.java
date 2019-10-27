package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;

import ch.bailu.aat.description.AverageSpeedDescriptionAP;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.PredictiveTimeDescription;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.SensorSource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.dispatcher.TrackerTimerSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.CockpitView;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.graph.GraphViewFactory;

public class CockpitActivity extends AbsKeepScreenOnActivity {

    private static final String SOLID_KEY="tracker";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EditorSource edit = new EditorSource(getServiceContext());
        ContentView contentView = new ContentView(this);
        MultiView multiView = createMultiView(edit);

        contentView.add(createButtonBar(multiView));
        contentView.add(multiView);

        setContentView(contentView);
        createDispatcher(edit);
    }


    private MultiView createMultiView(EditorSource edit) {
        MultiView multiView = new MultiView(this, SOLID_KEY);
        multiView.add(createCockpit());
        multiView.add(MapFactory.DEF(this, SOLID_KEY).tracker(edit).toView());
        multiView.add(GraphViewFactory.all(this, SOLID_KEY,this,  InfoID.TRACKER));

        return multiView;
    }


    private View createCockpit() {

        PercentageLayout p = new PercentageLayout(this);
        p.setOrientation(AppLayout.getOrientationAlongLargeSide(this));

        CockpitView c1 = new CockpitView(this);


        c1.add(this, new CurrentSpeedDescription(this),
                InfoID.SPEED_SENSOR, InfoID.LOCATION);

        c1.addAltitude(this);
        c1.add(this, new PredictiveTimeDescription(this), InfoID.TRACKER_TIMER);
        c1.addC(this, new DistanceDescription(this), InfoID.TRACKER);
        c1.addC(this, new AverageSpeedDescriptionAP(this), InfoID.TRACKER);

        CockpitView c2 = new CockpitView(this);
        c2.add(this, new MaximumSpeedDescription(this), InfoID.TRACKER);
        c2.addHeartRate(this);        // With click to update sensors
        c2.addCadence(this);          // With click to update sensors

        p.add(c1, 80);
        p.add(c2, 20);

        return p;
    }


    private ControlBar createButtonBar(MultiView mv) {
        MainControlBar bar = new MainControlBar(this);

        bar.addActivityCycle(this);
        bar.addMvNext(mv);
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
