package ch.bailu.aat.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.coordinates.Coordinates;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.util_java.util.Objects;

public class MapActivity extends AbsDispatcher{

    private static final String SOLID_KEY="map";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EditorHelper edit = new EditorHelper(getServiceContext());

        ContentView contentView=new ContentView(this);
        MapViewInterface map = createMap(edit);
        contentView.add(map.toView());
        setContentView(contentView);

        createDispatcher(edit);


        handleIntent(map);
    }


    private void handleIntent(MapViewInterface map) {
        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (Objects.equals(intent.getAction(),Intent.ACTION_VIEW) && uri != null) {
            setMapCenterFromUri(map, uri);
            openQueryFromUri(uri);
        }
    }

    private void setMapCenterFromUri(MapViewInterface map, Uri uri) {

        try {
            LatLong c = Coordinates.stringToGeoPoint(uri.toString());
            map.setCenter(c);

        } catch (NumberFormatException e) {
            AppLog.d(this, uri.toString());
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


    private MapViewInterface createMap(EditorHelper edit) {
        return MapFactory.DEF(this, SOLID_KEY).map(edit, createButtonBar());
    }


    private void createDispatcher(EditorHelper edit) {
        addSource(new EditorSource(getServiceContext(), edit));
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
