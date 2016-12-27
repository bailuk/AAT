package ch.bailu.aat.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.description.AccelerationDescription;
import ch.bailu.aat.description.AccuracyDescription;
import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.BearingDescription;
import ch.bailu.aat.description.CH1903EastingDescription;
import ch.bailu.aat.description.CH1903NorthingDescription;
import ch.bailu.aat.description.CaloriesDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.EndDateDescription;
import ch.bailu.aat.description.GpsStateDescription;
import ch.bailu.aat.description.LatitudeDescription;
import ch.bailu.aat.description.LongitudeDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.PauseDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.description.TrackerStateDescription;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.test.PreferencesFromSdcard;
import ch.bailu.aat.test.PreferencesToSdcard;
import ch.bailu.aat.test.TestCoordinates;
import ch.bailu.aat.test.TestGpx;
import ch.bailu.aat.test.TestGpxLogRecovery;
import ch.bailu.aat.test.TestTest;
import ch.bailu.aat.test.UnitTest;
import ch.bailu.aat.views.AbsLabelTextView;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.StatusTextView;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.Dem3NameOverlay;
import ch.bailu.aat.views.map.overlay.EndLogOverlay;
import ch.bailu.aat.views.map.overlay.StartLogOverlay;
import ch.bailu.aat.views.map.overlay.ZoomLevelOverlay;
import ch.bailu.aat.views.map.overlay.control.InformationBarOverlay;
import ch.bailu.aat.views.map.overlay.control.NavigationBarOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxOverlayListOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxTestOverlay;
import ch.bailu.aat.views.map.overlay.grid.GridDynOverlay;
import ch.bailu.aat.views.preferences.VerticalScrollView;

public class TestActivity extends AbsDispatcher {
    private static final String SOLID_KEY = "test";

    private OsmInteractiveView map;
    private StatusTextView statusTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LinearLayout contentView = new ContentView(this);

        MultiView multiView = createMultiView();

        contentView.addView(createButtonBar(multiView));
        contentView.addView(multiView);


        setContentView(contentView);

        createDispatcher();

    }


    private MultiView createMultiView() {
        map = new OsmInteractiveView(getServiceContext(), this, SOLID_KEY);

        ContentDescription locationDescription[] = new ContentDescription[]{
                new NameDescription(this),
                new GpsStateDescription(this),
                new AltitudeDescription(this),
                new LongitudeDescription(this),
                new LatitudeDescription(this),
                new CH1903EastingDescription(this),
                new CH1903NorthingDescription(this),
                new AccuracyDescription(this),
                new CurrentSpeedDescription(this),
                new AccelerationDescription(this),
                new BearingDescription(this),
        };

        ContentDescription trackerDescription[] = new ContentDescription[]{
                new NameDescription(this),
                new PathDescription(this),
                new TrackerStateDescription(this),
                new AverageSpeedDescription(this),
                new MaximumSpeedDescription(this),
                new CaloriesDescription(this),
                new DateDescription(this),
                new EndDateDescription(this),
                new TimeDescription(this),
                new PauseDescription(this),
                new TrackSizeDescription(this),
        };


        VerticalScrollView locationView = new VerticalScrollView(this);
        locationView.addAllContent(this, locationDescription, InfoID.LOCATION);

        VerticalScrollView trackerView = new VerticalScrollView(this);
        trackerView.addAllContent(this, trackerDescription, InfoID.TRACKER);

        VerticalScrollView testsView = new VerticalScrollView(this);

        testsView.add(new TestEntryView(new TestCoordinates(this)));
        testsView.add(new TestEntryView(new TestGpx(this)));
        testsView.add(new TestEntryView(new TestGpxLogRecovery(this)));
        testsView.add(new TestEntryView(new TestTest(this)));
        testsView.add(new TestEntryView(new PreferencesToSdcard(this)));
        testsView.add(new TestEntryView(new PreferencesFromSdcard(this)));

        VerticalScrollView speedView = new VerticalScrollView(this);

        speedView.add(this, new CurrentSpeedDescription(this), InfoID.LOCATION);
        speedView.add(this, new CurrentSpeedDescription(this), InfoID.TRACKER);


        statusTextView = new StatusTextView(this);

        MultiView mv = new MultiView(this, SOLID_KEY);


        mv.add(map,getString(R.string.intro_map));
        mv.add(locationView, getString(R.string.gps));

        mv.add(trackerView, getString(R.string.tracker));
        mv.add(speedView, "GPS vs Tracker*");

        mv.add(testsView, "Tests*");
        mv.add(statusTextView, getString(R.string.intro_status));


        return mv;
    }


    private ControlBar createButtonBar(MultiView multiView) {
        final MainControlBar bar = new MainControlBar(getServiceContext());

        bar.addAll(multiView);
        return bar;
    }



    private void createDispatcher() {
        StartLogOverlay start =new StartLogOverlay(map);

        map.add(start);
        map.add(new GpxOverlayListOverlay(map, this, getServiceContext()));
        map.add(new EndLogOverlay(map, start));

        map.add(new GpxTestOverlay(map, this, InfoID.OVERLAY));
        map.add(new GpxDynOverlay(map, getServiceContext(), this, InfoID.TRACKER));
//        map.setTarget(new CurrentLocationOverlay(map, this));

        map.add(new GridDynOverlay(map, getServiceContext()));

        map.add(new NavigationBarOverlay(map, this));
        map.add(new InformationBarOverlay(map, this));
        map.add(new ZoomLevelOverlay(map));
        map.add(new Dem3NameOverlay(map));


        addSource(new TrackerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));


    }

    @Override
    public void onResumeWithService() {
        super.onResumeWithService();
        statusTextView.updateText(this);
    }


    private class TestEntryView extends AbsLabelTextView {

        public TestEntryView(final UnitTest test) {
            super(test.getContext(), test.getClass().getSimpleName());

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        test.test();
                        setText("Test successfull");
                        AppLog.i(getContext(), "Test sucessfull");

                    } catch (AssertionError | Exception e) {
                        setText("Test failed.");
                        AppLog.e(getContext(), e);
                    }
                }

            });
        }
    }
}
