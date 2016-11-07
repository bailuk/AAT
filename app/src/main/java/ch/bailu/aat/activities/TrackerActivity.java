package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ch.bailu.aat.R;
import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.MvNextButton;
import ch.bailu.aat.views.description.CockpitView;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.description.TrackerStateButton;
import ch.bailu.aat.views.description.VerticalView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.graph.DistanceSpeedGraphView;
import ch.bailu.aat.views.map.MapFactory;
import ch.bailu.aat.views.map.OsmInteractiveView;

public class TrackerActivity extends AbsDispatcher implements OnClickListener{

    private static final String SOLID_KEY="tracker";

    private ImageButton          activityCycle;
    private TrackerStateButton   trackerState;
    private MultiView            multiView;
    private OsmInteractiveView   map;

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
        map = new MapFactory(this, SOLID_KEY).tracker(edit);

        multiView = new MultiView(this, SOLID_KEY);
        multiView.add(createCockpit());
        multiView.add(map);
        multiView.add(new VerticalView(this,
                new View[] {
                    new DistanceAltitudeGraphView(this, this, InfoID.TRACKER),
                    new DistanceSpeedGraphView(this, this, InfoID.TRACKER)
                }));

        return multiView;
    }


    private CockpitView createCockpit() {
        final ContentDescription[] data = new ContentDescription[] {
                new CurrentSpeedDescription(this),
                new AltitudeDescription(this),
                new TimeDescription(this),
                new DistanceDescription(this),
                new AverageSpeedDescription(this),
                new MaximumSpeedDescription(this),
        };

        return new CockpitView(this, this, data);
    }


    private ControlBar createButtonBar(MultiView mv) {
        ControlBar bar = new MainControlBar(getServiceContext());

        activityCycle = bar.addImageButton(R.drawable.go_down_inverse);
        bar.add(new MvNextButton(mv));

        trackerState = new TrackerStateButton(this.getServiceContext());

        bar.addView(trackerState);
        bar.setOnClickListener1(this);

        trackerState.setOnClickListener(trackerState);



        return bar;
    }


    private void createDispatcher() {
        addTarget(trackerState, InfoID.TRACKER);

        addSource(new EditorSource(getServiceContext(), edit));
        addSource(new TrackerSource(getServiceContext()));
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
