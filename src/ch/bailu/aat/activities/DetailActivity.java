package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import ch.bailu.aat.description.DescriptionInterface;
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
import ch.bailu.aat.dispatcher.ContentDispatcher;
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;
import ch.bailu.aat.services.tracker.TrackerService;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MultiView;
import ch.bailu.aat.views.NumberView;
import ch.bailu.aat.views.SummaryListView;
import ch.bailu.aat.views.TrackDescriptionView;
import ch.bailu.aat.R;

public class DetailActivity extends AbsDispatcher implements OnClickListener{
    public static final Class<?> SERVICES[] = {
        TrackerService.class,
    };    
    
    private final static String SOLID_KEY="detail";

    private SummaryListView gpsSummary, trackSummary;
    private LinearLayout    contentView;
    private Button     startPause;
    private ImageButton     multiCycle;
    private NumberView      trackerState;
    private MultiView       multiView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentView=new ContentView(this);
        contentView.addView(createButtonBar());
        multiView = createMultiView();
        contentView.addView(multiView);
        setContentView(contentView);

        connectToServices(SERVICES);
    }


    private MultiView createMultiView() {
        ContentDescription gpsData[] = new ContentDescription[] {
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

        ContentDescription trackData[] = new ContentDescription[] {
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

        gpsSummary= new SummaryListView(this, SOLID_KEY, INFO_ID_LOCATION, gpsData);
        trackSummary = new SummaryListView(this, SOLID_KEY, INFO_ID_TRACKER, trackData);

        return new MultiView(this, SOLID_KEY, INFO_ID_ALL,
                new TrackDescriptionView[] {gpsSummary, trackSummary});

    }


    private ControlBar createButtonBar() {
        ControlBar bar = new ControlBar(
                this, 
                AppLayout.getOrientationAlongSmallSide(this));

        startPause = bar.addButton("");
        multiCycle = bar.addImageButton(R.drawable.go_previous_inverse);

        trackerState = new NumberView(new TrackerStateDescription(this), 
                GpxInformation.ID.INFO_ID_TRACKER);
        bar.addView(trackerState);
        bar.setOnClickListener1(this);

        return bar;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v==startPause) {
            try {
                this.onStartPauseClick();
            } catch (ServiceNotUpException e) {
                AppLog.e(this, e);
            }

        } else if (v ==multiCycle) {
            multiView.setNext();
        }
    }


    @Override
    public void onServicesUp() {
        try {

            DescriptionInterface[] target = new DescriptionInterface[] {
                    multiView,trackerState, this
            };

            ContentSource[] source = new ContentSource[] {
                    new TrackerSource(getTrackerService()),
                    new CurrentLocationSource(getTrackerService())
            };

            setDispatcher(new ContentDispatcher(this,source, target));

        } catch (ServiceNotUpException e) {
            AppLog.e(this, e);
        }
    }


    @Override
    public void updateGpxContent(GpxInformation info) {
        updateStartButtonText(startPause, info);
    }


}
