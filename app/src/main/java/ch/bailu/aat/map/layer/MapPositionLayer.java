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
import ch.bailu.aat.preferences.SolidPositionLock;
import ch.bailu.aat.preferences.Storage;

public class MapPositionLayer implements MapLayerInterface, OnContentUpdatedInterface {

    private final MapContext mcontext;

    public static final String LONGITUDE_SUFFIX ="longitude";
    public static final String LATITUDE_SUFFIX ="latitude";
    private static final String ZOOM_SUFFIX ="zoom";

    private final SolidPositionLock slock;
    private LatLong gpsLocation = new LatLong(0,0);

    private final Storage storage;

    public MapPositionLayer(MapContext mc, DispatcherInterface d) {
        mcontext = mc;

        storage = Storage.global(mcontext.getContext());
        slock = new SolidPositionLock(mcontext.getContext(), mcontext.getSolidKey());

        loadState();

        d.addTarget(this, InfoID.LOCATION);
    }


    public void onMapCenterChanged(LatLong center) {
        if (gpsLocation.equals(center) == false) {
            disableLock();
        }
    }


    public void disableLock() {
        slock.setValue(false);
    }


    private void loadState() {
        gpsLocation = new LatLongE6(
                storage.readInteger(mcontext.getSolidKey() + LATITUDE_SUFFIX),
                storage.readInteger(mcontext.getSolidKey() + LONGITUDE_SUFFIX))
                .toLatLong();

        byte z = (byte) storage.readInteger(mcontext.getSolidKey() + ZOOM_SUFFIX);
        mcontext.getMapView().setZoomLevel(z);

        setMapCenter();
    }


    private void setMapCenter() {
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
    public void onLayout(boolean changed, int l, int t, int r, int b) {}


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (slock.hasKey(key)) {
            setMapCenter();
        }
    }


    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {
        saveState();
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        gpsLocation = LatLongE6.toLatLong(info);
        setMapCenter();
    }


    @Override
    public void drawInside(MapContext mcontext) {}

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        return false;
    }

    @Override
    public void drawOnTop(MapContext mcontext) {}
}
