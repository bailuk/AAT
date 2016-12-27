package ch.bailu.aat.services.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import java.util.List;

import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppPermission;
import ch.bailu.aat.helpers.ContextWrapperInterface;


@SuppressLint("MissingPermission")
public class RealLocation extends LocationStackChainedItem
        implements LocationListener, ContextWrapperInterface{

    private final String provider;
    private final Context context;
    private int state=-99;
    private LocationInformation lastLocation; 


    private class LocationWrapper extends LocationInformation {
        private final Location location;

        public LocationWrapper(Location l) {
            location = l;
            location.setTime(System.currentTimeMillis());
        }
        /*@Override
        public int getID() {
            return InfoID.LOCATION;
        }
        */
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
    }

    private class NoServiceException extends Exception {
        private static final long serialVersionUID = 5318663897402154115L;
    }


    public RealLocation(LocationStackItem i, Context c, String p) {
        super(i);

        context = c;
        provider = p;
        lastLocation = new LocationWrapper(new Location(provider));
    }
    
    
    public void init(int gpsInterval) {
        try {
            setState(StateID.WAIT);

            final LocationManager lm = getLocationManager(context);

            validateProvider(lm, provider);
            sendLastKnownLocation(lm, provider);
            requestLocationUpdates(lm, provider, gpsInterval);

        } catch (NoServiceException ex) {
            setState(StateID.NOSERVICE);
        } catch (SecurityException | IllegalArgumentException ex) {
            setState(StateID.NOACCESS);
        }

    }


    private void sendLastKnownLocation(LocationManager lm, String provider) {
        if (AppPermission.checkLocation(context)) {
            final Location loc = lm.getLastKnownLocation(provider);
            if (loc != null) sendLocation(loc);
        }
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
        if (AppPermission.checkLocation(c)) {
            final Object r = c.getSystemService(Context.LOCATION_SERVICE);

            if (r == null || LocationManager.class.isInstance(r) == false) {
                throw new NoServiceException();
            } else {
                return (LocationManager) r;
            }
        }
        throw new NoServiceException();
    }

    private void requestLocationUpdates(LocationManager lm, String provider, long interval)
            throws SecurityException, IllegalArgumentException {
        lm.requestLocationUpdates(provider , interval, 0, this);
    }

    @Override
    public void close() {
        try {
            getLocationManager(context).removeUpdates(this);
        } catch (NoServiceException e) {
            state=StateID.NOSERVICE;
        }
    }

    @Override
    public void onLocationChanged(Location l) {
        setState(StateID.ON);
        sendLocation(l);
    }


    public void sendLocation(Location l) {
        lastLocation= new LocationWrapper(l);
        sendLocation(lastLocation);
    }


    @Override
    public void onProviderDisabled(String p) {

        if (provider.equals(p)) {
            setState(StateID.OFF);
        }
    }

    @Override
    public void onProviderEnabled(String p) {

        if (provider.equals(p)) {
            setState(StateID.WAIT);
        }
    }


    @Override
    public void onStatusChanged(String p, int status, Bundle extras) {

        if (provider.equals(p)) {

            if (status == LocationProvider.AVAILABLE) {
                onProviderEnabled(p);
            } else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                onProviderEnabled(p);
            } else {
                onProviderDisabled(p);
            }
        }
    }

    public void setState(int s) {
        if (state != s) {
            state = s;
            sendState(s);
        }
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
        case StateID.NOACCESS: builder.append("STATE_NOACCESS"); break;
        case StateID.NOSERVICE: builder.append("STATE_NOSERVICE"); break;
        case StateID.ON: builder.append("STATE_ON"); break;
        case StateID.OFF: builder.append("STATE_OFF"); break;
        case StateID.PAUSE: builder.append("STATE_PAUSE"); break;
        case StateID.AUTOPAUSED: builder.append("STATE_AUTOPAUSED"); break;
        default: builder.append("STATE_WAIT"); break;
        }
        builder.append("<br>");

    }

}
