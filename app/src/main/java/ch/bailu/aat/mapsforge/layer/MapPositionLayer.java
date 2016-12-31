package ch.bailu.aat.mapsforge.layer;

import android.content.SharedPreferences;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.model.common.Observer;

import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.mapsforge.layer.context.MapContext;
import ch.bailu.aat.preferences.SolidPositionLock;
import ch.bailu.aat.preferences.Storage;

public class MapPositionLayer extends MapsForgeLayer implements OnContentUpdatedInterface, Observer {

    private final MapContext mcontext;
    private final MapView mapView;

    public static final String LONGITUDE_SUFFIX ="longitude";
    public static final String LATITUDE_SUFFIX ="latitude";
    private static final String ZOOM_SUFFIX ="zoom";

    private final SolidPositionLock slock;
    private LatLong gpsLocation = new LatLong(0,0);

    private final Storage storage;



    public MapPositionLayer(MapContext mc, DispatcherInterface d) {
        mcontext = mc;
        mapView = mc.mapView;

        storage = Storage.global(mcontext.context);
        slock = new SolidPositionLock(mcontext.context, mcontext.skey);

        loadState();

        d.addTarget(this, InfoID.LOCATION);
        mapView.getModel().mapViewPosition.addObserver(this);

    }



    private void loadState() {
        gpsLocation = new LatLongE6(
                storage.readInteger(mcontext.skey + LATITUDE_SUFFIX),
                storage.readInteger(mcontext.skey + LONGITUDE_SUFFIX))
                .toLatLong();

        byte z = (byte) storage.readInteger(mcontext.skey + ZOOM_SUFFIX);
        mapView.setZoomLevel(z);
        mapView.setCenter(gpsLocation);
    }


    private void refreshMap() {
        if (slock.isEnabled()) {
            mapView.setCenter(gpsLocation);
        }
    }

    private void saveState() {
        LatLongE6 point = new LatLongE6(mapView.getBoundingBox().getCenterPoint());
        int zoom = mapView.getModel().mapViewPosition.getZoomLevel();

        storage.writeInteger(mcontext.skey + LONGITUDE_SUFFIX, point.lo);
        storage.writeInteger(mcontext.skey + LATITUDE_SUFFIX, point.la);
        storage.writeInteger(mcontext.skey + ZOOM_SUFFIX, zoom);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (slock.hasKey(key)) {

            if (slock.isEnabled()) {
                mapView.setCenter(gpsLocation);
            }
        }
    }


    @Override
    public void onDetached() {
        saveState();
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        gpsLocation = new LatLongE6(info).toLatLong();
        refreshMap();
    }

    
    
    
    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {

    }

    @Override
    public void onChange() {
        // TODO disable lock when manually changed


        /*
        LatLong a = mapView.getModel().mapViewPosition.getMapPosition().latLong;
        LatLong b = gpsLocation;

        AppLog.d(this, a.toString());
        AppLog.d(this, b.toString());

        */
    }
}
