package ch.bailu.aat_lib.map.layer;

import org.mapsforge.core.model.LatLong;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.coordinates.LatLongE6;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.preferences.location.SolidMapPosition;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidPositionLock;

public final class MapPositionLayer implements MapLayerInterface, OnContentUpdatedInterface {

    private final MapContext mcontext;
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
        if (!gpsLocation.equals(center)) {
            disableLock();
        }
    }

    public void disableLock() {
        slock.setValue(false);
    }

    private void loadState() {
        gpsLocation = SolidMapPosition.readPosition(storage, mcontext.getSolidKey()).toLatLong();
        mcontext.getMapView().setZoomLevel((byte) 15);
        mcontext.getMapView().setCenter(gpsLocation);
    }

    private void setMapCenter() {
        if (slock.isEnabled()) {
            mcontext.getMapView().setCenter(gpsLocation);
        }
    }

    private void saveState() {
        LatLong center = mcontext.getMapView().getMapViewPosition().getCenter();
        SolidMapPosition.writePosition(storage, mcontext.getSolidKey(), center);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}


    @Override
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {
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
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
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
