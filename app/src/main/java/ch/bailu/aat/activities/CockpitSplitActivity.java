package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;

import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.AscendDescription;
import ch.bailu.aat.description.AverageSpeedDescriptionAP;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DescendDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.PredictiveTimeDescription;
import ch.bailu.aat.description.SlopeDescription;
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
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.graph.DistanceSpeedGraphView;

public class CockpitSplitActivity extends AbsDispatcher{
    private static final String SOLID_KEY="split";
    private static final String SOLID_MAP_KEY="themap";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EditorHelper edit = new EditorHelper(getServiceContext());
        setContentView(createContentView(edit));
        createDispatcher(edit);

    }


    private View createContentView(EditorHelper edit) {
        final MapViewInterface mapSlave = MapFactory.DEF(this, SOLID_KEY).split();
        final CockpitView cockpitA = new CockpitView(this);
        final CockpitView cockpitB = new CockpitView(this);
        final CockpitView cockpitC = new CockpitView(this);

        cockpitA.addC(this, new DistanceDescription(this), InfoID.TRACKER);
        cockpitA.add(this, new AltitudeDescription(this), InfoID.LOCATION);
        cockpitA.add(this, new PredictiveTimeDescription(this), InfoID.TRACKER_TIMER);

        cockpitB.add(this, new CurrentSpeedDescription(this), InfoID.LOCATION);
        cockpitB.addC(this, new AverageSpeedDescriptionAP(this), InfoID.TRACKER);

        cockpitC.addC(this, new DistanceDescription(this), InfoID.TRACKER);
        cockpitC.add(this, new AltitudeDescription(this), InfoID.LOCATION);
        cockpitC.add(this, new AscendDescription(this), InfoID.TRACKER);
        cockpitC.add(this, new DescendDescription(this), InfoID.TRACKER);
        cockpitC.add(this, new SlopeDescription(this), InfoID.TRACKER);


        MultiView mv = new MultiView(this, SOLID_KEY);
        mv.add(cockpitA);
        mv.add(cockpitB);
        mv.add(cockpitC);
        mv.add(new DistanceAltitudeGraphView(this, this, InfoID.TRACKER));
        mv.add(new DistanceSpeedGraphView(this, this, InfoID.TRACKER));
        mv.add(mapSlave.toView());

        MapViewInterface mapMaster =
                MapFactory.DEF(this, SOLID_MAP_KEY).map(edit, createButtonBar(mv));

        new MapViewLinker(mapMaster, mapSlave);

        return
                new PercentageLayout(this)
                        .add(mapMaster.toView(), 70)
                        .add(mv,30);
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


    private void createDispatcher(EditorHelper edit) {
        addSource(new EditorSource(getServiceContext(),edit));
        addSource(new TrackerSource(getServiceContext()));
        addSource(new TrackerTimerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));

    }
}
