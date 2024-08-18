package ch.bailu.aat_lib.service.location;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.coordinates.LatLongInterface;
import ch.bailu.aat_lib.broadcaster.AppBroadcaster;
import ch.bailu.aat_lib.broadcaster.Broadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.location.SolidMapPosition;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocName;

public final class DirtyLocation extends LocationStackChainedItem {
    private final static String SOLID_KEY="DirtyLocation_";

    private GpxInformation locationInformation;
    private int state = LocationService.INITIAL_STATE;

    private final StorageInterface storage;
    private final Broadcaster broadcast;


    public DirtyLocation(LocationStackItem n, StorageInterface s, Broadcaster b) {
        super(n);
        storage = s;
        broadcast = b;

        locationInformation = new OldLocation(storage);
    }


    public GpxInformation getLocationInformation() {
        return locationInformation;
    }

    @Override
    public void close() {
        SolidMapPosition.writePosition(storage, SOLID_KEY, locationInformation);
    }


    @Override
    public void passLocation(@Nonnull LocationInformation location) {
        locationInformation=location;
        super.passLocation(location);
        broadcast.broadcast(AppBroadcaster.LOCATION_CHANGED);

    }

    @Override
    public void passState(int s) {
        super.passState(s);
        state = s;
        broadcast.broadcast(AppBroadcaster.LOCATION_CHANGED);
    }


    class OldLocation extends GpxInformation  {
        private int longitude, latitude;

        private final Foc file;

        public OldLocation(StorageInterface storage) {
            file = new FocName(OldLocation.class.getSimpleName());
            readPosition(storage);
        }

        @Override
        public Foc getFile() {
            return file;
        }


        private void readPosition(StorageInterface storage) {
            LatLongInterface latLongE6 = SolidMapPosition.readPosition(storage, SOLID_KEY);

            longitude=latLongE6.getLongitudeE6();
            latitude= latLongE6.getLatitudeE6();
        }

        @Override
        public int getLongitudeE6() {
            return longitude;
        }

        @Override
        public int getLatitudeE6() {
            return latitude;
        }

        @Override
        public double getLongitude() {
            return ((double)longitude)/1e6d;
        }

        @Override
        public double getLatitude() {
            return ((double)latitude)/1e6d;
        }

        @Override
        public int getState() {
            return state;
        }

    }
}
