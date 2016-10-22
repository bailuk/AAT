package ch.bailu.aat.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;

import ch.bailu.aat.R;
import ch.bailu.aat.coordinates.Coordinates;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppIntent;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.description.GPSStateButton;
import ch.bailu.aat.views.description.NumberView;
import ch.bailu.aat.views.description.TrackerStateButton;
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


        handleIntent();
    }


    private void handleIntent() {
        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (intent.getAction().equals(Intent.ACTION_VIEW) && uri != null) {
            AppLog.d(uri, uri.toString());
            setMapCenterFromUri(uri);
            openQueryFromUri(uri);
        }
    }

    private void setMapCenterFromUri(Uri uri) {
        GeoPoint geo = new GeoPoint(0,0);

        if (Coordinates.stringToGeoPoint(uri.toString(), geo)) {
            map.map.getController().setCenter(geo);
        }
    }


    private void openQueryFromUri(Uri uri) {
        String query = AbsOsmApiActivity.queryFromUri(uri);

        if (query != null) {
            Intent intent = new Intent();
            AppIntent.setBoundingBox(intent, new BoundingBoxE6(0,0,0,0));
            intent.setData(uri);
            ActivitySwitcher.start(this, NominatimActivity.class, intent);
        }
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


    private void createDispatcher() {
        addTarget(map);
        addTarget(trackerState);
        addTarget(gpsState);

        addSource(new EditorSource(getServiceContext(), edit));
        addSource(new TrackerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));
    }



    @Override
    public void onClick(View v) {
        if (v==cycleButton) {
            ActivitySwitcher.cycle(this);
        }

    }


    private ControlBar createButtonBar() {
        ControlBar bar = new MainControlBar(getServiceContext());

        cycleButton = bar.addImageButton(R.drawable.go_down_inverse);

        gpsState = new GPSStateButton(this);
        trackerState = new TrackerStateButton(this.getServiceContext());

        bar.addView(gpsState);
        bar.addView(trackerState);

        bar.setOnClickListener1(this);

        return bar;
    }

}
