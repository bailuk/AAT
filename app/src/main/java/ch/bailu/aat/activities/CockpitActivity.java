package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.AverageSpeedDescriptionAP;
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
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.CockpitView;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.graph.GraphViewContainer;

public class CockpitActivity extends AbsDispatcher{

    private static final String SOLID_KEY="tracker";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EditorHelper edit = new EditorHelper(getServiceContext());
        ViewGroup contentView = new ContentView(this);
        MultiView multiView = createMultiView(edit);

        contentView.addView(createButtonBar(multiView));
        contentView.addView(multiView);

        setContentView(contentView);

        createDispatcher(edit);
    }


    private MultiView createMultiView(EditorHelper edit) {
        MultiView multiView = new MultiView(this, SOLID_KEY);
        multiView.add(createCockpit());
        multiView.add(MapFactory.DEF(this, SOLID_KEY).tracker(edit).toView());
        multiView.add(GraphViewContainer.speedAltitude(this, this,  InfoID.TRACKER, SOLID_KEY));

        return multiView;
    }


    private View createCockpit() {
        CockpitView c1 = new CockpitView(this);


        c1.add(this, new CurrentSpeedDescription(this), InfoID.LOCATION);
        c1.add(this, new AltitudeDescription(this), InfoID.LOCATION);
        c1.add(this, new PredictiveTimeDescription(this), InfoID.TRACKER_TIMER);
        c1.addC(this, new DistanceDescription(this), InfoID.TRACKER);
        c1.addC(this, new AverageSpeedDescriptionAP(this), InfoID.TRACKER);
        c1.add(this, new MaximumSpeedDescription(this), InfoID.TRACKER);

        return c1;
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


    private void createDispatcher(EditorHelper edit) {
        addSource(new EditorSource(getServiceContext(), edit));
        addSource(new TrackerSource(getServiceContext()));
        addSource(new TrackerTimerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));
    }
}
