package ch.bailu.aat.services.tracker.location;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.ContextWrapperInterface;

public class RealLocation extends LocationStackChainedItem implements LocationListener, GpxInformation.ID, ContextWrapperInterface{

    private String provider;
    private Context context;
    private int state=-99;
    private LocationInformation lastLocation; 


    private class LocationWrapper extends LocationInformation {
        private Location location;

        public LocationWrapper(Location l) {
            location = l;
            location.setTime(System.currentTimeMillis());
        }
        @Override
        public int getID() {
            return ID.INFO_ID_LOCATION;
        }
        @Override
        public int getState() {
            return state;
        }
        @Override
        public String getName() {
            return location.getProvider();
        }
        @Override 
        public float getAccuracy() {
            return location.getAccuracy();
        }
        @Override
        public float getSpeed() {
            return location.getSpeed();
        }
        @Override
        public short getAltitude() {
            return (short)location.getAltitude();
        }

        @Override
        public double getLatitude() {
            return location.getLatitude();
        }

        @Override
        public double getLongitude() {
            return location.getLongitude();
        }

        @Override
        public double getBearing() {
            return location.getBearing();
        }
        @Override
        public long getTimeStamp() {
            return location.getTime();
        }
        @Override
        public int getLatitudeE6() {
            return (int)(getLatitude()*1e6d);
        }
        @Override
        public int getLongitudeE6() {
            return (int)(getLongitude()*1e6d);
        }
        @Override
        public boolean hasAccuracy() {
            return location.hasAccuracy();
        }
        @Override
        public boolean hasSpeed() {
            return location.hasSpeed();
        }
        @Override
        public boolean hasAltitude() {
            return location.hasAltitude();
        }
        @Override
        public boolean hasBearing() {
            return location.hasBearing();
        }
    };

    private class NoServiceException extends Exception {
        private static final long serialVersionUID = 5318663897402154115L;
    }


    public RealLocation(LocationStackItem i, Context c, String p, int gpsInterval) {
        super(i);

        context = c;
        provider = p;
        lastLocation = new LocationWrapper(new Location(provider));

        try {
            setState(STATE_WAIT);

            final LocationManager lm = getLocationManager(c);

            validateProvider(lm, provider);
            sendLastKnownLocation(lm, provider);
            requestLocationUpdates(lm, provider, gpsInterval);

        } catch (NoServiceException ex) {
            setState(STATE_NOSERVICE);
        } catch (SecurityException ex) {
            setState(STATE_NOACCESS);
        } catch (IllegalArgumentException ex) {
            setState(STATE_NOACCESS);
        }
    }


    private void sendLastKnownLocation(LocationManager lm, String provider) {
        final Location loc = lm.getLastKnownLocation(provider);
        if (loc != null) sendLocation(loc);
    }

    private void validateProvider(LocationManager lm, String provider) throws NoServiceException {
        /* 
         *  On shashlik all access to LocationManager throws null pointer exception. 
         *  Therefore we catch all exceptions. 
         */
        
        try {
            List <String> list = lm.getAllProviders();

            if (list==null) {
                AppLog.d(this, "No providers");
                throw new NoServiceException();
            } else {
                AppLog.d(this, list.size() +" providers");
                for (int i=0; i<list.size(); i++) {
                    AppLog.d(this, list.get(i));
                }
            }

            if (lm.getProvider(provider)==null) {
                throw new NoServiceException();
            }
        } catch (Exception e) {
            throw new NoServiceException();
        }

    }
    private LocationManager getLocationManager(Context c) throws NoServiceException {
        final Object r = c.getSystemService(Context.LOCATION_SERVICE);

        if (r==null || LocationManager.class.isInstance(r)==false) {
            throw new NoServiceException();
        } else {
            return (LocationManager) r;
        }
    }

    private void requestLocationUpdates(LocationManager lm, String provider, long interval) throws SecurityException, IllegalArgumentException {
        lm.requestLocationUpdates(provider , interval, 0, this);
    }

    @Override
    public void close() {
        try {
            getLocationManager(context).removeUpdates(this);
        } catch (NoServiceException e) {
            state=STATE_NOSERVICE;
        }
    }

    @Override
    public void onLocationChanged(Location l) {
        setState(STATE_ON);
        sendLocation(l);
    }


    public void sendLocation(Location l) {
        lastLocation= new LocationWrapper(l);
        sendLocation(lastLocation);
    }


    @Override
    public void onProviderDisabled(String p) {
        setState(STATE_OFF);
    }

    @Override
    public void onProviderEnabled(String p) {
        setState(STATE_WAIT);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (status == LocationProvider.AVAILABLE) {
            onProviderEnabled(provider);
        } else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE)  {
            onProviderEnabled(provider);
        } else {
            onProviderDisabled(provider);
        }
    }

    public void setState(int s) {
        if (state != s) sendState(s);
    }

    @Override
    public void sendState(int s) {
        state=s;
        super.sendState(state);
    }


    @Override
    public void newLocation(LocationInformation location) {
        sendLocation(location);
    }

    @Override
    public void preferencesChanged(Context c, int i) {}

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void appendStatusText(StringBuilder builder) {
        super.appendStatusText(builder);
        builder.append("Provider: ");
        builder.append(provider);
        builder.append("<br>");

        switch (state) {
        case STATE_NOACCESS: builder.append("STATE_NOACCESS"); break;
        case STATE_NOSERVICE: builder.append("STATE_NOSERVICE"); break;
        case STATE_ON: builder.append("STATE_ON"); break;
        case STATE_OFF: builder.append("STATE_OFF"); break;
        case STATE_PAUSE: builder.append("STATE_PAUSE"); break;
        case STATE_AUTOPAUSED: builder.append("STATE_AUTOPAUSED"); break;
        default: builder.append("STATE_WAIT"); break;
        }
        builder.append("<br>");

    }

}
