package ch.bailu.aat.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.R;
import ch.bailu.aat.coordinates.WGS84Coordinates;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.util_java.util.Objects;

public class MapActivity extends AbsKeepScreenOnActivity {

    private static final String SOLID_KEY="map";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EditorSource edit = new EditorSource(getServiceContext());

        ContentView contentView=new ContentView(this, AppTheme.cockpit);
        MapViewInterface map = createMap(edit);
        contentView.add(map.toView());
        setContentView(contentView);

        createDispatcher(edit);


        contentView.showTip(getString(R.string.tt_map_edges));

        handleIntent(map);
    }


    private void handleIntent(MapViewInterface map) {
        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (Objects.equals(intent.getAction(),Intent.ACTION_VIEW) && uri != null) {
            setMapCenterFromUri(map, uri);
        }
    }

    private void setMapCenterFromUri(MapViewInterface map, Uri uri) {

        try {
            LatLong c = new WGS84Coordinates(uri.toString()).toLatLong();
            map.setCenter(c);

        } catch (Exception e) {
            AppLog.w(this, e);
        }
    }


    private MapViewInterface createMap(EditorSource edit) {
        return MapFactory.DEF(this, SOLID_KEY).map(edit, createButtonBar());
    }


    private void createDispatcher(EditorSource edit) {
        addSource(edit);
        addSource(new TrackerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));
    }



    private ControlBar createButtonBar() {
        MainControlBar bar = new MainControlBar(this);

        bar.addActivityCycle(this);

        if (AppLayout.haveExtraSpaceGps(this)) {
            bar.addSpace();
        }
        bar.addGpsState(this);
        bar.addTrackerState(this);

        return bar;
    }

}
