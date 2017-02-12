package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ch.bailu.aat.R;
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
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.MvNextButton;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.description.CockpitView;
import ch.bailu.aat.views.description.GPSStateButton;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.description.TrackerStateButton;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.graph.DistanceSpeedGraphView;

public class TrackerActivity extends AbsDispatcher implements OnClickListener{

    private static final String SOLID_KEY="tracker";

    private ImageButton          activityCycle;
    private MultiView            multiView;

    private EditorHelper         edit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edit = new EditorHelper(getServiceContext());

        ViewGroup contentView = new ContentView(this);

        multiView = createMultiView();

        contentView.addView(createButtonBar(multiView));
        contentView.addView(multiView);

        setContentView(contentView);

        createDispatcher();
    }


    private MultiView createMultiView() {
        multiView = new MultiView(this, SOLID_KEY);
        multiView.add(createCockpit());
        multiView.add(MapFactory.DEF(this, SOLID_KEY).tracker(edit).toView());
        multiView.add(PercentageLayout.add(this,
                        new DistanceAltitudeGraphView(this, this, InfoID.TRACKER),
                        new DistanceSpeedGraphView(this, this, InfoID.TRACKER)));

        return multiView;
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


    private ControlBar createButtonBar(MultiView mv) {
        MainControlBar bar = new MainControlBar(this);

        activityCycle = bar.addImageButton(R.drawable.go_down_inverse);
        bar.add(new MvNextButton(mv));



        if (AppLayout.haveExtraSpaceGps(this)) {
            bar.addGpsState(this);
        }


        bar.addTrackerState(this);
        bar.setOnClickListener1(this);

        return bar;
    }


    private void createDispatcher() {


        addSource(new EditorSource(getServiceContext(), edit));
        addSource(new TrackerSource(getServiceContext()));
        addSource(new TrackerTimerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));

    }


    @Override
    public void onClick(View v) {
        if (v == activityCycle) {
            ActivitySwitcher.cycle(this);

        }
    }
}
