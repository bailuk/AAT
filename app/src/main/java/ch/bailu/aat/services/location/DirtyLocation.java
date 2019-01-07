package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.map.layer.MapPositionLayer;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.foc.FocName;

public class DirtyLocation extends LocationStackChainedItem {
    private final static String SOLID_KEY="DirtyLocation_";

    private GpxInformation locationInformation;
    private int state = RealLocation.INITIAL_STATE;

    private final Storage storage;
    
    
    public DirtyLocation(LocationStackItem n, Context c) {
        super(n);
        storage = new Storage(c);

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
    public void passLocation(LocationInformation location) {
        locationInformation=location;
        super.passLocation(location);
        AppBroadcaster.broadcast(storage.getContext(), AppBroadcaster.LOCATION_CHANGED);
    }


    @Override
    public void passState(int s) {
        super.passState(s);
        state = s;
        AppBroadcaster.broadcast(storage.getContext(), AppBroadcaster.LOCATION_CHANGED);
    }

    @Override
    public void preferencesChanged(Context c, int i) {}
    

    
    class OldLocation extends GpxInformation  {
        private int longitude, latitude;

        private final Foc file;

        public OldLocation(Storage storage) {
            file = new FocName(OldLocation.class.getSimpleName());
            readPosition(storage);
        }
        
        @Override
        public Foc getFile() {
            return file;
        }


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


    /*
    private final static DateFormat TIME_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private void log(LocationInformation location) {
        AppLog.d(this, location.getFile().getName()
                + ": "
                + TIME_FORMAT.format(location.getTimeStamp()));
    }
    */

}
