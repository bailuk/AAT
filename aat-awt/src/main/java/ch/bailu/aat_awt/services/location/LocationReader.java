package ch.bailu.aat_awt.services.location;

import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;

import ch.bailu.aat_lib.coordinates.LatLongE6;
import ch.bailu.aat_lib.service.location.LocationInformation;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocName;

public class LocationReader extends LocationInformation {

    private final LatLongE6 latLongE6;
    private final float accuracy;
    private final float speed;
    private final long time;
    private double altitude;
    private final long creationTime;

    public LocationReader(DBusConnection connection, DBusInterface location) throws DBusException {

        LocationProperties properties = new LocationProperties(connection, location);

        latLongE6 = new LatLongE6(properties.readDouble("Latitude"), properties.readDouble("Longitude"));
        accuracy = properties.readDouble("Accuracy").floatValue();
        speed = properties.readDouble("Speed").floatValue();
        altitude = properties.readDouble("Altitude");

        time = properties.readTimeStamp();
        creationTime = System.currentTimeMillis();
    }


    @Override
    public Foc getFile() {
        return new FocName(this.getClass().getSimpleName());
    }

    @Override
    public long getTimeStamp() {
        return time;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAccuracy() {
        return accuracy;
    }

    @Override
    public double getLongitude() {
        return latLongE6.getLongitude();
    }

    @Override
    public double getLatitude() {
        return latLongE6.getLatitude();
    }

    @Override
    public int getLongitudeE6() {
        return latLongE6.getLongitudeE6();
    }

    @Override
    public int getLatitudeE6() {
        return latLongE6.getLatitudeE6();
    }


    @Override
    public boolean hasAccuracy() {
        return true;
    }

    @Override
    public boolean hasSpeed() {
        return true;
    }

    @Override
    public boolean hasAltitude() {
        return true;
    }

    @Override
    public boolean hasBearing() {
        return true;
    }

    @Override
    public boolean isFromGPS() {
        // TODO how do we know
        return true;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public double getAltitude() {
        return altitude;
    }
}
