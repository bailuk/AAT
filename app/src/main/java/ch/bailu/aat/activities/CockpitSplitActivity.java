package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;

import ch.bailu.aat.description.AscendDescription;
import ch.bailu.aat.description.AveragePaceDescription;
import ch.bailu.aat.description.AverageSpeedDescriptionAP;
import ch.bailu.aat.description.CadenceDescription;
import ch.bailu.aat.description.CurrentPaceDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DescendDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.HeartRateDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.PaceDescription;
import ch.bailu.aat.description.PredictiveTimeDescription;
import ch.bailu.aat.description.SlopeDescription;
import ch.bailu.aat.description.StepRateDescription;
import ch.bailu.aat.description.TotalStepsDescription;
import ch.bailu.aat.dispatcher.SensorSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.dispatcher.TrackerTimerSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.mapsforge.MapViewLinker;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.CockpitView;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.graph.DistanceSpeedGraphView;
import ch.bailu.aat.views.graph.SpmGraphView;

public class CockpitSplitActivity extends AbsKeepScreenOnActivity {
    private static final String SOLID_KEY="split";
    private static final String SOLID_MAP_KEY="themap";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EditorSource edit = new EditorSource(getServiceContext());
        setContentView(createContentView(edit));
        createDispatcher(edit);
    }


    private View createContentView(EditorSource edit) {
        final MapViewInterface mapSlave = MapFactory.DEF(this, SOLID_KEY).split();
        final CockpitView cockpitA = new CockpitView(this);
        final CockpitView cockpitB = new CockpitView(this);
        final CockpitView cockpitC = new CockpitView(this);
        final CockpitView cockpitD = new CockpitView(this);

        PercentageLayout percentageB = new PercentageLayout(this);
        percentageB.setOrientation(AppLayout.getOrientationAlongLargeSide(this));

        PercentageLayout percentageC = new PercentageLayout(this);
        percentageC.setOrientation(AppLayout.getOrientationAlongLargeSide(this));

        PercentageLayout percentageD = new PercentageLayout(this);
        percentageD.setOrientation(AppLayout.getOrientationAlongLargeSide(this));

        cockpitA.add(this, new CurrentSpeedDescription(this), InfoID.SPEED_SENSOR, InfoID.LOCATION);
        cockpitA.addC(this, new DistanceDescription(this), InfoID.TRACKER);
        cockpitA.addC(this, new AverageSpeedDescriptionAP(this), InfoID.TRACKER);
        cockpitA.add(this, new PredictiveTimeDescription(this), InfoID.TRACKER_TIMER);

        cockpitB.addC(this, new AveragePaceDescription(this), InfoID.TRACKER);
        cockpitB.addC(this, new AverageSpeedDescriptionAP(this), InfoID.TRACKER);
        cockpitB.addC(this, new MaximumSpeedDescription(this), InfoID.TRACKER);

        percentageB.add(cockpitB, 50);
        percentageB.add(new DistanceSpeedGraphView(this, this, InfoID.TRACKER), 50);

        cockpitD.addAltitude(this);
        cockpitD.add(this, new AscendDescription(this), InfoID.TRACKER);
        cockpitD.add(this, new DescendDescription(this), InfoID.TRACKER);
        cockpitD.add(this, new SlopeDescription(this), InfoID.TRACKER);

        percentageD.add(cockpitD, 50);
        percentageD.add(new DistanceAltitudeGraphView(this, this, InfoID.TRACKER), 50);

        cockpitC.add(this, new CadenceDescription(this), InfoID.CADENCE_SENSOR);
        cockpitC.add(this, new HeartRateDescription(this), InfoID.HEART_RATE_SENSOR);
        cockpitC.add(this, new StepRateDescription(this), InfoID.STEP_COUNTER_SENSOR);
        cockpitC.add(this, new TotalStepsDescription(this), InfoID.TRACKER);

        percentageC.add(cockpitC, 50);
        percentageC.add(new SpmGraphView(this, this, InfoID.TRACKER), 50);

        MultiView mv = new MultiView(this, SOLID_KEY);
        mv.add(cockpitA);
        mv.add(percentageB);
        mv.add(percentageC);
        mv.add(percentageD);
        mv.add(mapSlave.toView());

        MapViewInterface mapMaster =
                MapFactory.DEF(this, SOLID_MAP_KEY).map(edit, createButtonBar(mv));

        new MapViewLinker(mapMaster, mapSlave);

        ContentView contentView = new ContentView(this);

        contentView.add(
                new PercentageLayout(this)
                        .add(mapMaster.toView(), 70)
                        .add(mv,30));

        return contentView;
    }






    private ControlBar createButtonBar(MultiView mv) {
        final MainControlBar bar = new MainControlBar(this);

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
        addSource(new SensorSource(getServiceContext(), InfoID.STEP_COUNTER_SENSOR));



    }
}
