package ch.bailu.aat.services.tracker.location;

import android.content.Context;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.views.map.OsmInteractiveView;

public class DirtyLocation extends LocationStackChainedItem {
    private final static String SOLID_KEY="DirtyLocation_";

    
    private GpxInformation locationInformation;
    private int state = GpxInformation.ID.STATE_OFF;

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
    public void cleanUp() {
        storage.writeInteger(SOLID_KEY + OsmInteractiveView.LONGITUDE_SUFFIX, locationInformation.getLongitudeE6());
        storage.writeInteger(SOLID_KEY + OsmInteractiveView.LATITUDE_SUFFIX, locationInformation.getLatitudeE6());
    }

    
    @Override
    public void newLocation(LocationInformation location) {
        locationInformation=location;
        AppBroadcaster.broadcast(storage.getContext(), AppBroadcaster.LOCATION_CHANGED);
        sendLocation(location);
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
            return "old location*";
        }
        @Override
        public int getID() {
            return ID.INFO_ID_LOCATION;
        }

        private void readPosition(Storage storage) {
            longitude=storage.readInteger(SOLID_KEY + OsmInteractiveView.LONGITUDE_SUFFIX);
            latitude=storage.readInteger(SOLID_KEY + OsmInteractiveView.LATITUDE_SUFFIX);
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
        
    };

}
