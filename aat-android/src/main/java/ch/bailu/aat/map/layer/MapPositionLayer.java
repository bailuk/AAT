package ch.bailu.aat.map.layer;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.map.SolidPositionLock;
import ch.bailu.aat_lib.coordinates.LatLongE6;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class MapPositionLayer implements MapLayerInterface, OnContentUpdatedInterface {

    private final MapContext mcontext;

    public static final String LONGITUDE_SUFFIX ="longitude";
    public static final String LATITUDE_SUFFIX ="latitude";
    private static final String ZOOM_SUFFIX ="zoom";

    private final SolidPositionLock slock;

    private LatLong gpsLocation = new LatLong(0,0);

    private final Storage storage;

    public MapPositionLayer(MapContext mc, DispatcherInterface d) {
        mcontext = mc;

        storage = new Storage(mcontext.getContext());
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

        storage.writeInteger(mcontext.getSolidKey() + LATITUDE_SUFFIX, center.la);
        storage.writeInteger(mcontext.getSolidKey() + LONGITUDE_SUFFIX, center.lo);
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
    public void onAttached() {
       // loadState();
    }

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
    public boolean onTap(Point tapXY) {
        return false;
    }

    @Override
    public void drawForeground(MapContext mcontext) {}
}
