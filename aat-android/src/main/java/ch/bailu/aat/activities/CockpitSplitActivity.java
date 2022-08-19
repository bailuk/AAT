package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;

import ch.bailu.aat.util.AndroidTimer;
import ch.bailu.aat.views.graph.GraphViewFactory;
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource;
import ch.bailu.aat_lib.description.EditorSource;
import ch.bailu.aat_lib.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.SensorSource;
import ch.bailu.aat_lib.dispatcher.TrackerSource;
import ch.bailu.aat_lib.dispatcher.TrackerTimerSource;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.mapsforge.MapViewLinker;
import ch.bailu.aat.map.mapsforge.MapsForgeViewBase;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.CockpitView;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat_lib.description.AscendDescription;
import ch.bailu.aat_lib.description.AveragePaceDescription;
import ch.bailu.aat_lib.description.AverageSpeedDescriptionAP;
import ch.bailu.aat_lib.description.CadenceDescription;
import ch.bailu.aat_lib.description.CurrentSpeedDescription;
import ch.bailu.aat_lib.description.DescendDescription;
import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat_lib.description.HeartRateDescription;
import ch.bailu.aat_lib.description.MaximumSpeedDescription;
import ch.bailu.aat_lib.description.PowerDescription;
import ch.bailu.aat_lib.description.PredictiveTimeDescription;
import ch.bailu.aat_lib.description.SlopeDescription;
import ch.bailu.aat_lib.description.StepRateDescription;
import ch.bailu.aat_lib.description.TotalStepsDescription;
import ch.bailu.aat_lib.gpx.InfoID;

public class CockpitSplitActivity extends AbsKeepScreenOnActivity {
    private static final String SOLID_KEY="split";
    public static final String SOLID_MAP_KEY="themap";

    private static final UiTheme THEME = AppTheme.cockpit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EditorSource edit = new EditorSource(getAppContext());
        setContentView(createContentView(edit));
        createDispatcher(edit);
    }



    private View createContentView(EditorSource edit) {
        final MapsForgeViewBase mapSlave = MapFactory.DEF(this, SOLID_KEY).split();
        final CockpitView cockpitA = new CockpitView(this, THEME);
        final CockpitView cockpitB = new CockpitView(this, THEME);
        final CockpitView cockpitC = new CockpitView(this, THEME);
        final CockpitView cockpitD = new CockpitView(this, THEME);

        PercentageLayout percentageB = new PercentageLayout(this);
        percentageB.setOrientation(AppLayout.getOrientationAlongLargeSide(this));

        PercentageLayout percentageC = new PercentageLayout(this);
        percentageC.setOrientation(AppLayout.getOrientationAlongLargeSide(this));

        PercentageLayout percentageD = new PercentageLayout(this);
        percentageD.setOrientation(AppLayout.getOrientationAlongLargeSide(this));

        cockpitA.add(this, new CurrentSpeedDescription(getStorage()),
                InfoID.SPEED_SENSOR, InfoID.LOCATION);
        cockpitA.addC(this, new AverageSpeedDescriptionAP(getStorage()), InfoID.TRACKER);
        cockpitA.addC(this, new AveragePaceDescription(getStorage()), InfoID.TRACKER);
        cockpitA.addC(this, new DistanceDescription(getStorage()), InfoID.TRACKER);
        cockpitA.add(this, new PredictiveTimeDescription(),
                InfoID.TRACKER_TIMER);

        cockpitB.addC(this, new AveragePaceDescription(getStorage()), InfoID.TRACKER);
        cockpitB.addC(this, new AverageSpeedDescriptionAP(getStorage()), InfoID.TRACKER);
        cockpitB.addC(this, new MaximumSpeedDescription(getStorage()), InfoID.TRACKER);

        percentageB.add(cockpitB, 50);
        percentageB.add(GraphViewFactory.createSpeedGraph(getAppContext(), this, THEME)
                .connect(this, InfoID.TRACKER), 50);

        cockpitD.addAltitude(this);
        cockpitD.add(this, new AscendDescription(new Storage(this)), InfoID.TRACKER);
        cockpitD.add(this, new DescendDescription(new Storage(this)), InfoID.TRACKER);
        cockpitD.add(this, new SlopeDescription(), InfoID.TRACKER);

        percentageD.add(cockpitD, 50);
        percentageD.add(GraphViewFactory.createAltitudeGraph(getAppContext(), this,THEME)
                        .connect(this, InfoID.TRACKER), 50);

        cockpitC.add(this, new CadenceDescription(), InfoID.CADENCE_SENSOR);
        cockpitC.add(this, new HeartRateDescription(), InfoID.HEART_RATE_SENSOR);
        cockpitC.add(this, new PowerDescription(), InfoID.POWER_SENSOR);
        cockpitC.add(this, new StepRateDescription(), InfoID.STEP_COUNTER_SENSOR);
        cockpitC.add(this, new TotalStepsDescription(), InfoID.TRACKER);

        percentageC.add(cockpitC, 50);
        percentageC.add(GraphViewFactory.createSpmGraph(getAppContext(), this, THEME)
                .connect(this, InfoID.TRACKER), 50);

        MultiView mv = new MultiView(this, SOLID_KEY);
        mv.add(cockpitA);
        mv.add(percentageB);
        mv.add(percentageC);
        mv.add(percentageD);
        mv.add(mapSlave);

        MapsForgeViewBase mapMaster =
                MapFactory.DEF(this, SOLID_MAP_KEY).map(edit, createButtonBar(mv));

        new MapViewLinker(mapMaster, mapSlave);

        ContentView contentView = new ContentView(this, THEME);

        contentView.addMvIndicator(mv);
        contentView.add(getErrorView());
        contentView.add(
                new PercentageLayout(this)
                        .add(mapMaster, 70)
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
        addSource(new TrackerSource(getServiceContext(),getBroadcaster()));
        addSource(new TrackerTimerSource(getServiceContext(), new AndroidTimer()));
        addSource(new CurrentLocationSource(getServiceContext(),getBroadcaster()));
        addSource(new OverlaySource(getAppContext()));
        addSource(new SensorSource(getServiceContext(), InfoID.HEART_RATE_SENSOR));
        addSource(new SensorSource(getServiceContext(), InfoID.POWER_SENSOR));
        addSource(new SensorSource(getServiceContext(), InfoID.CADENCE_SENSOR));
        addSource(new SensorSource(getServiceContext(), InfoID.SPEED_SENSOR));
        addSource(new SensorSource(getServiceContext(), InfoID.STEP_COUNTER_SENSOR));
    }
}
