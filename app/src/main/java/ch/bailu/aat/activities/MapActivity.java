package ch.bailu.aat.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.osmdroid.util.BoundingBoxOsm;
import org.osmdroid.util.GeoPoint;

import ch.bailu.aat.R;
import ch.bailu.aat.coordinates.Coordinates;
import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.description.GPSStateButton;
import ch.bailu.aat.views.description.NumberView;
import ch.bailu.aat.views.description.TrackerStateButton;

public class MapActivity extends AbsDispatcher implements OnClickListener{

    private static final String SOLID_KEY="map";

    private MapViewInterface      map;

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
        contentView.addView(map.toView());
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
            map.setCenter(new LatLongE6(geo).toLatLong());
        }
    }


    private void openQueryFromUri(Uri uri) {
        String query = AbsOsmApiActivity.queryFromUri(uri);

        if (query != null) {
            Intent intent = new Intent();
            AppIntent.setBoundingBox(intent, new BoundingBoxOsm(0,0,0,0));
            intent.setData(uri);
            ActivitySwitcher.start(this, NominatimActivity.class, intent);
        }
    }


    private MapViewInterface createMap() {
        return MapFactory.DEF(this, SOLID_KEY).map(edit, createButtonBar());
    }


    private void createDispatcher() {
        addTarget(trackerState, InfoID.TRACKER);
        addTarget(gpsState, InfoID.LOCATION);

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
