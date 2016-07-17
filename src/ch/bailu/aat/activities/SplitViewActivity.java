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
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.dispatcher.ContentDispatcher;
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.views.CockpitView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.MultiView;
import ch.bailu.aat.views.TrackDescriptionView;
import ch.bailu.aat.views.TrackerStateButton;
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

public class SplitViewActivity extends AbsDispatcher implements OnClickListener{
    private static final String SOLID_KEY="split";
    private static final String SOLID_MAP_KEY="themap";

    private MultiView       multiView;
    private OsmInteractiveView      mapView, mapViewAlt;
    private ImageButton             activityCycle, multiCycle;
    private TrackerStateButton      trackerState;

    private EditorHelper          edit;
    
    
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


        v.addView(createMapView(), LayoutParams.MATCH_PARENT, AppLayout.getScreenSmallSide(this));
        v.addView(new TextView(this), LayoutParams.MATCH_PARENT, 3);
        v.addView(createMultiView());
        return v;
    }



    private View createMapView() {
        mapView = new OsmInteractiveView(getServiceContext(), SOLID_MAP_KEY);
        return mapView;
    }


    private TrackDescriptionView createMultiView() {
        ContentDescription[] cockpitA = new ContentDescription[] { 
                new DistanceDescription(this),
                new AltitudeDescription(this),
                new TimeDescription(this),
        };

        ContentDescription[] cockpitB = new ContentDescription[] { 
                new CurrentSpeedDescription(this),
                new AverageSpeedDescription(this),
        };


        mapViewAlt=new OsmInteractiveView(getServiceContext(), SOLID_KEY);

        OsmOverlay overlayList[] = {
                new GpxOverlayListOverlay(mapViewAlt,getServiceContext()),
                new GpxDynOverlay(mapViewAlt, getServiceContext(), GpxInformation.ID.INFO_ID_TRACKER), 
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
        ControlBar bar = new MainControlBar(this);

        activityCycle = bar.addImageButton(R.drawable.go_down_inverse);
        multiCycle = bar.addImageButton(R.drawable.go_next_inverse);

        trackerState = new TrackerStateButton(this.getServiceContext());
        bar.addView(trackerState);
        bar.setOnClickListener1(this);
        
        return bar;
    }



    @Override
    public void onClick(View v) {
        if (v == activityCycle) {
            ActivitySwitcher.cycle(this);

        } else if (v ==multiCycle) {
            multiView.setNext();
        }
    }




    @Override
    public void onDestroy() {
        edit.close();
        super.onDestroy();
    }


    
    private void createDispatcher() {
            
            OsmOverlay overlayList[] = {
                    new GpxOverlayListOverlay(mapView, getServiceContext()),
                    new GpxDynOverlay(mapView, getServiceContext(), GpxInformation.ID.INFO_ID_TRACKER), 
                    new CurrentLocationOverlay(mapView),
                    new GridDynOverlay(mapView, getServiceContext()),
                    new NavigationBarOverlay(mapView),
                    new InformationBarOverlay(mapView),
                    new CustomBarOverlay(mapView, createButtonBar()),
                    new EditorOverlay(mapView, getServiceContext(),  GpxInformation.ID.INFO_ID_EDITOR_DRAFT, edit),

            };
            mapView.setOverlayList(overlayList);


            DescriptionInterface[] target = new DescriptionInterface[] {
                    multiView, trackerState, mapView, this
            };

            ContentSource[] source = new ContentSource[] {
                    new EditorSource(getServiceContext(),edit),
                    new TrackerSource(getServiceContext()),
                    new CurrentLocationSource(getServiceContext()),
                    new OverlaySource(getServiceContext()),
            };

            setDispatcher(new ContentDispatcher(this,source, target));

    }
    
    @Override
    public void onServicesUp(boolean firstRun) {}


}
