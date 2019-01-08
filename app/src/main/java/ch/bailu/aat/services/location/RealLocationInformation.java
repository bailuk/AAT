package ch.bailu.aat.services.location;

import android.location.Location;

import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.foc.FocName;

public class RealLocationInformation extends LocationInformation {
    private final Location location;
    private final int state;
    private final FocName provider;

    public RealLocationInformation(Location l, int s) {
        provider = new FocName(l.getProvider());
        state = s;
        location = l;
    }

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

    @Override
    public boolean isFromGPS() {
        return false;
    }

    @Override
    public long getCreationTime() {
        return getTimeStamp();
    }

    @Override
    public void setAltitude(double altitude) {
        location.setAltitude(altitude);
    }
}
