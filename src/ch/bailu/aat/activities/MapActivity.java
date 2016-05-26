package ch.bailu.aat.activities;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import ch.bailu.aat.R;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.description.GpsStateDescription;
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
import ch.bailu.aat.services.ServiceContext.ServiceNotUpException;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.dem.ElevationService;
import ch.bailu.aat.services.editor.EditorService;
import ch.bailu.aat.services.overlay.OverlayService;
import ch.bailu.aat.services.tracker.TrackerService;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.NumberView;
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

public class MapActivity extends AbsDispatcher implements OnClickListener{
    private static final Class<?> SERVICES[] = {
        TrackerService.class, 
        OverlayService.class,
        ElevationService.class,
        CacheService.class,
        EditorService.class
    };    
    
    private static final String SOLID_KEY="map";

    private OsmInteractiveView      map;

    private Button          startPause;
    private ImageButton     cycleButton;
    private NumberView      trackerState, gpsState;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        
        LinearLayout contentView=new ContentView(this);
        map = createMap();
        contentView.addView(map);
        setContentView(contentView);

        connectToServices(SERVICES);        
    }


    private OsmInteractiveView createMap() {
        OsmInteractiveView map=new OsmInteractiveView(getServiceContext(), SOLID_KEY);
        return map;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private ControlBar createButtonBar() {
        ControlBar bar = new ControlBar(
                this, 
                AppLayout.getOrientationAlongSmallSide(this));

        startPause = bar.addButton("");
        cycleButton = bar.addImageButton(R.drawable.go_down_inverse);

        gpsState = new NumberView(new GpsStateDescription(this),
                GpxInformation.ID.INFO_ID_LOCATION);
        trackerState = new NumberView(new TrackerStateDescription(this), 
                GpxInformation.ID.INFO_ID_TRACKER);

        bar.addView(gpsState);
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

        } if (v==cycleButton) {
            ActivitySwitcher.cycle(this);
        }

    }

    @Override
    public void onServicesUp() {
        try {
            OsmOverlay overlayList[] = {
                    
                    new GpxOverlayListOverlay(map, getServiceContext().getCacheService()),
                    new GpxDynOverlay(map, getServiceContext().getCacheService(), GpxInformation.ID.INFO_ID_TRACKER), 
                    new GridDynOverlay(map, getServiceContext().getElevationService()),
                    new CurrentLocationOverlay(map),
                    new NavigationBarOverlay(map),
                    new InformationBarOverlay(map),
                    new EditorOverlay(map, getServiceContext().getCacheService(), GpxInformation.ID.INFO_ID_EDITOR_DRAFT, 
                            getServiceContext().getEditorService().getDraftEditor(), getServiceContext().getElevationService()),
                            
                    new CustomBarOverlay(map, createButtonBar()),
            };
            map.setOverlayList(overlayList);
            
            
            DescriptionInterface[] target = new DescriptionInterface[] {
                    map,trackerState,gpsState, this
            };

            ContentSource[] source = new ContentSource[] {
                    new EditorSource(getServiceContext().getEditorService(),GpxInformation.ID.INFO_ID_EDITOR_DRAFT),
                    new TrackerSource(getServiceContext().getTrackerService()),
                    new CurrentLocationSource(getServiceContext().getTrackerService()),
                    new OverlaySource(getServiceContext().getOverlayService())};

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
