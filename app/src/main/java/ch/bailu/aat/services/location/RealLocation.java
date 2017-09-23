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
import ch.bailu.aat.util.AppPermission;
import ch.bailu.aat.util.ContextWrapperInterface;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.foc.FocName;


@SuppressLint("MissingPermission")
public class RealLocation extends LocationStackChainedItem
        implements LocationListener, ContextWrapperInterface{

    private final FocName provider;
    private final Context context;

    private int state = -99;

    private Location location;
    private final LocationInformation locationInformation = new  LocationInformation () {
        @Override
        public int getState() {
            return state;
        }
        @Override
        public Foc getFile() {
            return provider;
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
            return (short)Math.round(location.getAltitude());
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



    public RealLocation(LocationStackItem i, Context c, String p, int intervall) {
        super(i);

        context = c;
        provider = new FocName(p);
        location = new Location(provider.getName());

        init(intervall);
    }
    
    
    private void init(int gpsInterval) {
        try {
            passState(StateID.WAIT);

            final LocationManager lm = getLocationManager(context);

            validateProvider(lm, provider.getName());
            sendLastKnownLocation(lm, provider.getName());
            requestLocationUpdates(lm, provider.getName(), gpsInterval);

        } catch (NoServiceException ex) {
            passState(StateID.NOSERVICE);
        } catch (SecurityException | IllegalArgumentException ex) {
            passState(StateID.NOACCESS);
        }

    }


    private void sendLastKnownLocation(LocationManager lm, String provider) {
        if (AppPermission.checkLocation(context)) {
            final Location loc = lm.getLastKnownLocation(provider);
            if (loc != null) locationChange(loc);
        }
    }






    private void validateProvider(LocationManager lm, String provider) throws NoServiceException {
        try {
            List <String> list = lm.getAllProviders();

            if (list==null) {
                throw new NoServiceException();
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

            if (r instanceof LocationManager) {
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
            AppLog.d(this, "=> removeUpdates()");
            getLocationManager(context).removeUpdates(this);
        } catch (Exception e) {
            state = StateID.NOSERVICE;
        }
    }

    @Override
    public void onLocationChanged(Location l) {
        passState(StateID.ON);
        locationChange(l);
    }


    public void locationChange(Location l) {
        location = l;
        passLocation(locationInformation);
    }


    @Override
    public void onProviderDisabled(String p) {
        if (provider.getName().equals(p)) {
            passState(StateID.OFF);
        }
    }

    @Override
    public void onProviderEnabled(String p) {

        if (provider.getName().equals(p)) {
            passState(StateID.WAIT);
        }
    }


    @Override
    public void onStatusChanged(String p, int status, Bundle extras) {

        if (provider.getName().equals(p)) {

            if (status == LocationProvider.AVAILABLE) {
                onProviderEnabled(p);
            } else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                onProviderEnabled(p);
            } else {
                onProviderDisabled(p);
            }
        }
    }

    @Override
    public void passState(int s) {
        if (state != s) {
            state = s;
            super.passState(s);
        }
    }



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
