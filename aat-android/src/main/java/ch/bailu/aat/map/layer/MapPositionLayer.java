package ch.bailu.aat.map.layer;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.preferences.map.SolidPositionLock;
import ch.bailu.aat_lib.coordinates.LatLongE6;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class MapPositionLayer implements MapLayerInterface, OnContentUpdatedInterface {

    private final MapContext mcontext;

    public static final String LONGITUDE_SUFFIX ="longitude";
    public static final String LATITUDE_SUFFIX ="latitude";
    private static final String ZOOM_SUFFIX ="zoom";

    private final SolidPositionLock slock;

    private LatLong gpsLocation = new LatLong(0,0);

    private final StorageInterface storage;

    public MapPositionLayer(MapContext mc, StorageInterface storage, DispatcherInterface d) {
        mcontext = mc;
        this.storage = storage;

        slock = new SolidPositionLock(storage, mcontext.getSolidKey());

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
        mcontext.getMapView().setCenter(gpsLocation);
    }


    private void setMapCenter() {
        if (slock.isEnabled()) {
            mcontext.getMapView().setCenter(gpsLocation);
        }
    }

    private void saveState() {
        LatLongE6 center = new LatLongE6(mcontext.getMapView().getMapViewPosition().getCenter());

        int zoom = mcontext.getMetrics().getZoomLevel();

        storage.writeInteger(mcontext.getSolidKey() + LATITUDE_SUFFIX, center.getLatitudeE6());
        storage.writeInteger(mcontext.getSolidKey() + LONGITUDE_SUFFIX, center.getLongitudeE6());
        storage.writeInteger(mcontext.getSolidKey() + ZOOM_SUFFIX, zoom);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}


    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {
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
    public void drawForeground(MapContext mcontext) {}

    @Override
    public boolean onTap(Point tapPos) {
        return false;
    }
}
