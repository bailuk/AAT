package ch.bailu.aat.activities;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
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
import ch.bailu.aat.description.OnContentUpdatedInterface;
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
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.RootDispatcher;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.GpxInformation;
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
import ch.bailu.aat.views.map.overlay.CurrentLocationOverlay;
import ch.bailu.aat.views.map.overlay.Dem3NameOverlay;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.RefreshLogOverlay;
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

    private MultiView multiView;

    private OsmInteractiveView map;

    private StatusTextView statusTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LinearLayout contentView = new ContentView(this);


        multiView = createMultiView();

        contentView.addView(createButtonBar(multiView));
        contentView.addView(multiView);


        setContentView(contentView);

        createDispatcher();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private MultiView createMultiView() {
        map = new OsmInteractiveView(getServiceContext(), SOLID_KEY);

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
        VerticalScrollView trackerView = new VerticalScrollView(this);
        VerticalScrollView testsView = new VerticalScrollView(this);

        testsView.add(new TestEntryView(new TestCoordinates(this)));
        testsView.add(new TestEntryView(new TestGpx(this)));
        testsView.add(new TestEntryView(new TestGpxLogRecovery(this)));
        testsView.add(new TestEntryView(new TestTest(this)));
        testsView.add(new TestEntryView(new PreferencesToSdcard(this)));
        testsView.add(new TestEntryView(new PreferencesFromSdcard(this)));


        statusTextView = new StatusTextView(this);


        MultiView mv = new MultiView(this, SOLID_KEY, GpxInformation.ID.INFO_ID_ALL);

        mv.addT(map,"Map*");
        mv.add(locationView, locationView.addAllContent(
                locationDescription, GpxInformation.ID.INFO_ID_LOCATION), "Location*");

        mv.add(trackerView, trackerView.addAllContent(
                trackerDescription, GpxInformation.ID.INFO_ID_TRACKER), "Tracker*");
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
        OsmOverlay overlayList[] = {
                new GpxOverlayListOverlay(map, getServiceContext()),
                new GpxDynOverlay(map, getServiceContext(), GpxInformation.ID.INFO_ID_TRACKER),
                new GpxTestOverlay(map, GpxInformation.ID.INFO_ID_OVERLAY),
                new GridDynOverlay(map, getServiceContext()),
                new CurrentLocationOverlay(map),
                new NavigationBarOverlay(map),
                new InformationBarOverlay(map),
                new ZoomLevelOverlay(map),
                new RefreshLogOverlay(map),
                new Dem3NameOverlay(map)
        };
        map.setOverlayList(overlayList);


        OnContentUpdatedInterface[] target = new OnContentUpdatedInterface[]{
                multiView, this
        };

        ContentSource[] source = new ContentSource[]{
                new TrackerSource(getServiceContext()),
                new CurrentLocationSource(getServiceContext()),
                new OverlaySource(getServiceContext()),
        };
        setDispatcher(new RootDispatcher(this, source, target));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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
