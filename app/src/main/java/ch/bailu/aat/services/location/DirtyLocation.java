package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.map.layer.MapPositionLayer;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;

public class DirtyLocation extends LocationStackChainedItem {
    private final static String SOLID_KEY="DirtyLocation_";

    
    private GpxInformation locationInformation;
    private int state = StateID.OFF;

    private final Storage storage;
    
    
    public DirtyLocation(LocationStackItem n, Context c) {
        super(n);
        storage = Storage.global(c);
        
        locationInformation = new OldLocation(storage);
    }

    
    public GpxInformation getLocationInformation() {
        return locationInformation;
    }
    
    @Override
    public void close() {
        storage.writeInteger(SOLID_KEY + MapPositionLayer.LONGITUDE_SUFFIX, locationInformation.getLongitudeE6());
        storage.writeInteger(SOLID_KEY + MapPositionLayer.LATITUDE_SUFFIX, locationInformation.getLatitudeE6());
    }

    
    @Override
    public void newLocation(LocationInformation location) {
        AppLog.d(this, "newLocation() -> send");
        locationInformation=location;
        sendLocation(location);
        AppBroadcaster.broadcast(storage.getContext(), AppBroadcaster.LOCATION_CHANGED);
    }
    
    @Override
    public void newState(int s) {
        super.newState(s);
        state = s;
        AppBroadcaster.broadcast(storage.getContext(), AppBroadcaster.LOCATION_CHANGED);
    }

    @Override
    public void preferencesChanged(Context c, int i) {}
    

    
    class OldLocation extends GpxInformation  {
        private int longitude, latitude;

        public OldLocation(Storage storage) {
            readPosition(storage);
        }
        
        @Override
        public String getName() {
            return storage.getContext().getString(R.string.p_location_old);
        }

        /*
        @Override
        public int getID() {
            return InfoID.LOCATION;
        }
*/

        private void readPosition(Storage storage) {
            longitude=storage.readInteger(SOLID_KEY + MapPositionLayer.LONGITUDE_SUFFIX);
            latitude=storage.readInteger(SOLID_KEY + MapPositionLayer.LATITUDE_SUFFIX);
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
