package ch.bailu.aat.activities;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import ch.bailu.aat.R;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.description.GpsStateDescription;
import ch.bailu.aat.dispatcher.ContentDispatcher;
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.NumberView;
import ch.bailu.aat.views.TrackerStateButton;
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

    private static final String SOLID_KEY="map";

    private OsmInteractiveView      map;

    private ImageButton     cycleButton;
    private NumberView      gpsState;
    private TrackerStateButton trackerState;

    private EditorHelper    edit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edit = new EditorHelper(getServiceContext());

        LinearLayout contentView=new ContentView(this);
        map = createMap();
        contentView.addView(map);
        setContentView(contentView);

        createDispatcher();
    }


    private OsmInteractiveView createMap() {
        final ServiceContext sc=getServiceContext();
        final OsmInteractiveView map=new OsmInteractiveView(sc, SOLID_KEY);

        OsmOverlay overlayList[] = {
                new GpxOverlayListOverlay(map, sc),
                new GpxDynOverlay(map, sc, GpxInformation.ID.INFO_ID_TRACKER), 
                new GridDynOverlay(map, sc),
                new CurrentLocationOverlay(map),
                new NavigationBarOverlay(map),
                new InformationBarOverlay(map),
                new EditorOverlay(map, sc, GpxInformation.ID.INFO_ID_EDITOR_DRAFT, edit),
                new CustomBarOverlay(map, createButtonBar()),
        };
        map.setOverlayList(overlayList);

        return map;
    }



    @Override
    public void onDestroy() {
        edit.close();
        super.onDestroy();
    }


    @Override
    public void onServicesUp(boolean firstRun) {
        edit.edit();
        super.onServicesUp(firstRun);
    }


    @Override
    public void onClick(View v) {
        if (v==cycleButton) {
            ActivitySwitcher.cycle(this);
        }

    }


    private void createDispatcher() {
        DescriptionInterface[] target = new DescriptionInterface[] {
                map,trackerState,gpsState, this
        };

        ContentSource[] source = new ContentSource[] {
                new EditorSource(getServiceContext(), edit),
                new TrackerSource(getServiceContext()),
                new CurrentLocationSource(getServiceContext()),
                new OverlaySource(getServiceContext())};

        setDispatcher(new ContentDispatcher(this,source, target));
    }


    private ControlBar createButtonBar() {
        ControlBar bar = new MainControlBar(this);

        cycleButton = bar.addImageButton(R.drawable.go_down_inverse);

        gpsState = new NumberView(new GpsStateDescription(this),
                GpxInformation.ID.INFO_ID_LOCATION);
        trackerState = new TrackerStateButton(this.getServiceContext());

        bar.addView(gpsState);
        bar.addView(trackerState);

        bar.setOnClickListener1(this);
        
        return bar;
    }

}
