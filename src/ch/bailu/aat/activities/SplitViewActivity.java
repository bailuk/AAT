package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.description.TrackerStateDescription;
import ch.bailu.aat.dispatcher.ContentDispatcher;
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.editor.EditorService;
import ch.bailu.aat.services.overlay.OverlayService;
import ch.bailu.aat.services.srtm.ElevationService;
import ch.bailu.aat.services.tracker.TrackerService;
import ch.bailu.aat.views.CockpitView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MultiView;
import ch.bailu.aat.views.NumberView;
import ch.bailu.aat.views.TrackDescriptionView;
import ch.bailu.aat.views.VerticalView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.graph.DistanceSpeedGraphView;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.CurrentLocationOverlay;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.control.CustomBarOverlay;
import ch.bailu.aat.views.map.overlay.control.EditorOverlay;
import ch.bailu.aat.views.map.overlay.control.InformationBarOverlay;
import ch.bailu.aat.views.map.overlay.control.NavigationBarOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxOverlayListOverlay;
import ch.bailu.aat.views.map.overlay.grid.GridDynOverlay;
import ch.bailu.aat.R;

public class SplitViewActivity extends AbsDispatcher implements OnClickListener{
    private static final Class<?> SERVICES[] = {
        TrackerService.class, 
        OverlayService.class,
        ElevationService.class,
        CacheService.class,
        EditorService.class
    };    
    
    
    private static final String SOLID_KEY="split";
    private static final String SOLID_MAP_KEY="themap";

    private MultiView       multiView;
    private OsmInteractiveView      mapView, mapViewAlt;
    private Button     startPause;
    private ImageButton     activityCycle, multiCycle;
    private NumberView      trackerState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        connectToServices(SERVICES);
    }


    private View createContentView() throws ServiceNotUpException {
        LinearLayout v = new LinearLayout(this);
        v.setOrientation(LinearLayout.VERTICAL);


        v.addView(createMapView(), LayoutParams.MATCH_PARENT, AppLayout.getScreenSmallSide(this));
        v.addView(new TextView(this), LayoutParams.MATCH_PARENT, 3);
        v.addView(createMultiView());
        return v;
    }



    private View createMapView() {
        mapView = new OsmInteractiveView(this, SOLID_MAP_KEY);
        return mapView;
    }


    private TrackDescriptionView createMultiView() throws ServiceNotUpException {
        ContentDescription[] cockpitA = new ContentDescription[] { 
                new DistanceDescription(this),
                new AltitudeDescription(this),
                new TimeDescription(this),
        };

        ContentDescription[] cockpitB = new ContentDescription[] { 
                new CurrentSpeedDescription(this),
                new AverageSpeedDescription(this),
        };


        mapViewAlt=new OsmInteractiveView(this, SOLID_KEY);

        OsmOverlay overlayList[] = {
                new GpxOverlayListOverlay(mapViewAlt,getCacheService()),
                new GpxDynOverlay(mapViewAlt, getCacheService(), GpxInformation.ID.INFO_ID_TRACKER), 
                new CurrentLocationOverlay(mapViewAlt),
                new NavigationBarOverlay(mapViewAlt,6),
        };
        mapViewAlt.setOverlayList(overlayList);


        TrackDescriptionView viewData[] = {
                new CockpitView(this, SOLID_KEY, INFO_ID_TRACKER, cockpitA),
                new CockpitView(this, SOLID_KEY, INFO_ID_TRACKER, cockpitB),
                new VerticalView(this, SOLID_KEY, INFO_ID_TRACKER, new TrackDescriptionView[] {
                        new DistanceAltitudeGraphView(this, SOLID_KEY),
                        new DistanceSpeedGraphView(this, SOLID_KEY)}),   
                        mapViewAlt,
        };   



        multiView = new MultiView(this, SOLID_KEY, INFO_ID_ALL, viewData);



        return multiView;
    }


    private ControlBar createButtonBar() {
        ControlBar bar = new ControlBar(
                this, 
                AppLayout.getOrientationAlongSmallSide(this));

        startPause = bar.addButton("");
        activityCycle = bar.addImageButton(R.drawable.go_down_inverse);
        multiCycle = bar.addImageButton(R.drawable.go_next_inverse);

        trackerState = new NumberView(new TrackerStateDescription(this), 
                GpxInformation.ID.INFO_ID_TRACKER);
        bar.addView(trackerState);

        bar.setOnClickListener1(this);
        return bar;
    }



    @Override
    public void onClick(View v) {
        if (v==startPause) {
            try {
                onStartPauseClick();
            } catch (ServiceNotUpException e) {
                AppLog.e(this, e);
            }


        } else if (v == activityCycle) {
            ActivitySwitcher.cycle(this);

        } else if (v ==multiCycle) {
            multiView.setNext();
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onServicesUp() {
        try {
            setContentView(createContentView());
            
            mapView.setServices(getCacheService());
            OsmOverlay overlayList[] = {
                    new GpxOverlayListOverlay(mapView, getCacheService()),
                    new GpxDynOverlay(mapView, getCacheService(), GpxInformation.ID.INFO_ID_TRACKER), 
                    new CurrentLocationOverlay(mapView),
                    new GridDynOverlay(mapView, getElevationService()),
                    new NavigationBarOverlay(mapView),
                    new InformationBarOverlay(mapView),
                    new CustomBarOverlay(mapView, createButtonBar()),
                    new EditorOverlay(mapView, getCacheService(),  GpxInformation.ID.INFO_ID_EDITOR_DRAFT, 
                            getEditorService().getDraftEditor(), getElevationService()),

            };
            mapView.setOverlayList(overlayList);


            mapViewAlt.setServices(getCacheService());    		


            DescriptionInterface[] target = new DescriptionInterface[] {
                    multiView, trackerState, mapView, this
            };

            ContentSource[] source = new ContentSource[] {
                    new EditorSource(getEditorService(),GpxInformation.ID.INFO_ID_EDITOR_DRAFT),
                    new TrackerSource(getTrackerService()),
                    new CurrentLocationSource(getTrackerService()),
                    new OverlaySource((OverlayService)getService(OverlayService.class)),
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
