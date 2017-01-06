package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.R;
import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.PredictiveTimeDescription;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.dispatcher.TrackerTimerSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.description.CockpitView;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.description.TrackerStateButton;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.graph.DistanceSpeedGraphView;

public class SplitViewActivity extends AbsDispatcher implements OnClickListener{
    private static final String SOLID_KEY="split";
    private static final String SOLID_MAP_KEY="themap";

    private MultiView               multiView;
    private ImageButton             activityCycle, multiCycle;
    private EditorHelper            edit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edit = new EditorHelper(getServiceContext());
        setContentView(createContentView());
        createDispatcher();

    }


    private View createContentView() {
        LinearLayout v = new LinearLayout(this);
        v.setOrientation(LinearLayout.VERTICAL);


        v.addView(MapFactory.DEF(this, SOLID_MAP_KEY).map(edit, createButtonBar()).toView(),
                LayoutParams.MATCH_PARENT,
                AppLayout.getScreenSmallSide(this));

        v.addView(new TextView(this), LayoutParams.MATCH_PARENT, 3);
        v.addView(createMultiView());
        return v;
    }




    private View createMultiView() {
        final MapViewInterface mapViewAlt = MapFactory.DEF(this, SOLID_KEY).split();
        final CockpitView cockpitA = new CockpitView(this);
        final CockpitView cockpitB = new CockpitView(this);


        cockpitA.addC(this, new DistanceDescription(this), InfoID.TRACKER);
        cockpitA.add(this, new AltitudeDescription(this), InfoID.LOCATION);
        cockpitA.add(this, new PredictiveTimeDescription(this), InfoID.TRACKER_TIMER);

        cockpitB.add(this, new CurrentSpeedDescription(this), InfoID.LOCATION);
        cockpitB.addC(this, new AverageSpeedDescription(this), InfoID.TRACKER);


        multiView = new MultiView(this, SOLID_KEY);
        multiView.add(cockpitA);
        multiView.add(cockpitB);
        multiView.add(new DistanceAltitudeGraphView(this, this, InfoID.TRACKER));
        multiView.add(new DistanceSpeedGraphView(this, this, InfoID.TRACKER));
        multiView.add(mapViewAlt.toView());

        return multiView;
    }


    private ControlBar createButtonBar() {
        ControlBar bar = new MainControlBar(getServiceContext());

        activityCycle = bar.addImageButton(R.drawable.go_down_inverse);
        multiCycle = bar.addImageButton(R.drawable.go_next_inverse);

        TrackerStateButton trackerState = new TrackerStateButton(this.getServiceContext());
        bar.addView(trackerState);
        bar.setOnClickListener1(this);

        addTarget(trackerState, InfoID.TRACKER);


        return bar;
    }


    private void createDispatcher() {
        addSource(new EditorSource(getServiceContext(),edit));
        addSource(new TrackerSource(getServiceContext()));
        addSource(new TrackerTimerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));

    }

    @Override
    public void onClick(View v) {
        if (v == activityCycle) {
            ActivitySwitcher.cycle(this);

        } else if (v ==multiCycle) {
            multiView.setNext();
        }
    }
}
