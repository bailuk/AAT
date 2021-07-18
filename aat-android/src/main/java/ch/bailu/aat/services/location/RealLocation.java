package ch.bailu.aat.services.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import java.util.List;

import ch.bailu.aat.util.AppPermission;
import ch.bailu.aat.util.ContextWrapperInterface;
import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.service.location.LocationInformation;
import ch.bailu.aat_lib.service.location.LocationStackChainedItem;
import ch.bailu.aat_lib.service.location.LocationStackItem;
import ch.bailu.util.Objects;


@SuppressLint("MissingPermission")
public class RealLocation extends LocationStackChainedItem
        implements LocationListener, ContextWrapperInterface{


    public final static int INITIAL_STATE = StateID.WAIT;


    private int state = INITIAL_STATE;

    private final String provider;
    private final Context context;



    public static final class NoServiceException extends Exception {
        private static final long serialVersionUID = 5318663897402154115L;
    }


    public RealLocation(LocationStackItem i, Context c, String p, int intervall) {
        super(i);


        context = c;
        provider = p;

        try {
            final LocationManager lm = getLocationManager();

            validateProvider(lm);
            sendLastKnownLocation(lm);
            requestLocationUpdates(lm, intervall);

            passState(StateID.WAIT);

        } catch (NoServiceException ex) {
            passState(StateID.NOSERVICE);
        } catch (SecurityException | IllegalArgumentException ex) {
            passState(StateID.NOACCESS);
        }

    }


    private void sendLastKnownLocation(LocationManager lm) {
        if (AppPermission.checkLocation(context)) {
            final Location l = lm.getLastKnownLocation(provider);
            if (l != null)
                passLocation(new RealLocationInformation(l, state));
        }
    }


    private void validateProvider(LocationManager lm) throws NoServiceException {
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

    private LocationManager getLocationManager() throws NoServiceException {
        return getLocationManager(context);
    }

    public static LocationManager getLocationManager(Context context) throws NoServiceException {
        if (AppPermission.checkLocation(context)) {
            final Object lm = context.getSystemService(Context.LOCATION_SERVICE);

            if (lm instanceof LocationManager) {
                return (LocationManager) lm;
            }
        }
        throw new NoServiceException();
    }


    public static List<String> getAllLocationProvidersOrNull(Context c) {
        try {
            LocationManager lm = RealLocation.getLocationManager(c);
            return lm.getAllProviders();

        } catch (RealLocation.NoServiceException e) {
            return null;
        }
    }

    private void requestLocationUpdates(LocationManager lm, long interval)
            throws SecurityException, IllegalArgumentException {
        lm.requestLocationUpdates(provider , interval, 0, this);
    }

    @Override
    public void close() {
        try {
            getLocationManager().removeUpdates(this);
        } catch (Exception e) {
            state = StateID.NOSERVICE;
        }
    }

    @Override
    public void onLocationChanged(Location l) {

        if (isMine(l)) {
            passState(StateID.ON);
            passLocation(factoryLocationInformation(l, state));
        }
    }


    protected LocationInformation factoryLocationInformation(Location location, int state) {
        return new RealLocationInformation(location, state);
    }



    private boolean isMine(Location l) {
        return l != null && isMine(l.getProvider());
    }
    private boolean isMine(String s) {
        return Objects.equals(s, provider);
    }




    @Override
    public void onProviderDisabled(String p) {
        if (isMine(p)) {
            passState(StateID.OFF);
        }
    }

    @Override
    public void onProviderEnabled(String p) {

        if (isMine(p)) {
            passState(StateID.WAIT);
        }
    }


    @Override
    public void onStatusChanged(String p, int status, Bundle extras) {
        if (isMine(p)) {
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
