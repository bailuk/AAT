package ch.bailu.aat.map.layer;

import android.content.SharedPreferences;
import android.view.MotionEvent;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.preferences.SolidPositionLock;
import ch.bailu.aat.preferences.Storage;

public class MapPositionLayer implements MapLayerInterface, OnContentUpdatedInterface {

    private final MapContext mcontext;

    private static final float UNLOCK_TRIGGER_SIZE = 50;

    public static final String LONGITUDE_SUFFIX ="longitude";
    public static final String LATITUDE_SUFFIX ="latitude";
    private static final String ZOOM_SUFFIX ="zoom";

    private final SolidPositionLock slock;
    private LatLong gpsLocation = new LatLong(0,0);

    private final Storage storage;

    private float motionX=0f, motionY=0f;


    public MapPositionLayer(MapContext mc, DispatcherInterface d) {
        mcontext = mc;


        storage = Storage.global(mcontext.getContext());
        slock = new SolidPositionLock(mcontext.getContext(), mcontext.getSolidKey());

        loadState();

        d.addTarget(this, InfoID.LOCATION);


    }



    private void loadState() {
        gpsLocation = new LatLongE6(
                storage.readInteger(mcontext.getSolidKey() + LATITUDE_SUFFIX),
                storage.readInteger(mcontext.getSolidKey() + LONGITUDE_SUFFIX))
                .toLatLong();

        byte z = (byte) storage.readInteger(mcontext.getSolidKey() + ZOOM_SUFFIX);
        mcontext.getMapView().setZoomLevel(z);
        mcontext.getMapView().setCenter(gpsLocation);
    }


    private void refreshMap() {
        if (slock.isEnabled()) {
            mcontext.getMapView().setCenter(gpsLocation);
        }
    }

    private void saveState() {
        LatLongE6 point = new LatLongE6(mcontext.getMetrics().getBoundingBox().getCenterPoint());
        int zoom = mcontext.getMetrics().getZoomLevel();

        storage.writeInteger(mcontext.getSolidKey() + LONGITUDE_SUFFIX, point.lo);
        storage.writeInteger(mcontext.getSolidKey() + LATITUDE_SUFFIX, point.la);
        storage.writeInteger(mcontext.getSolidKey() + ZOOM_SUFFIX, zoom);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (slock.hasKey(key)) {

            if (slock.isEnabled()) {
                mcontext.getMapView().setCenter(gpsLocation);
            }
        }
    }


    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {
        saveState();
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        gpsLocation = LatLongE6.toLatLong(info);
        refreshMap();
    }


    @Override
    public void drawInside(MapContext mcontext) {
    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        return false;
    }

    @Override
    public void drawOnTop(MapContext mcontext) {

    }

    public void onTouch(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            motionX = event.getX();
            motionY = event.getY();

        } else if (event.getAction()==MotionEvent.ACTION_UP && slock.isEnabled()) {
            float size =
                    Math.abs(motionX - event.getX()) +
                            Math.abs(motionY - event.getY());

            if (size > UNLOCK_TRIGGER_SIZE)
                slock.setValue(false);
        }
    }
}
