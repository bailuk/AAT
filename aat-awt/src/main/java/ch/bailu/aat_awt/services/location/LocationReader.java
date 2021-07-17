package ch.bailu.aat_awt.services.location;

import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.interfaces.Properties;
import org.freedesktop.dbus.types.UInt64;

import ch.bailu.aat_lib.service.location.LocationInformation;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocName;

public class LocationReader extends LocationInformation {
    private final static String LOCATION_INTERFACE = "org.freedesktop.GeoClue2.Location";
    private final Double latitude;
    private final Double longitude;
    private final Double accuracy;
    private final Double speed;
    private final Long time;

    private Double altitude;


    private final String description;

    private final Properties properties;

    public LocationReader(DBusConnection connection, DBusInterface location) throws DBusException {
        Object times[];

        properties = connection.getRemoteObject(GeoClue2Dbus.BUS_NAME, location.getObjectPath(), Properties.class);


        latitude = readDouble("Latitude");
        longitude = readDouble("Longitude");
        accuracy = readDouble("Accuracy");
        speed = readDouble("Speed");
        altitude = readDouble("Altitude");




        description = readDescription();

        times = properties.Get(LOCATION_INTERFACE, "Timestamp");

        time = ((UInt64) times[0]).longValue()*1000;
    }


    private Double readDouble(String key) {
        return properties.Get(LOCATION_INTERFACE, key);
    }

    private String readDescription() {
        return properties.Get(LOCATION_INTERFACE, "Description");
    }

    @Override
    public Foc getFile() {
        return new FocName(description);
    }

    @Override
    public long getTimeStamp() {
        return time;
    }

    @Override
    public float getSpeed() {
        return speed.floatValue();
    }

    @Override
    public float getAccuracy() {
        return accuracy.floatValue();
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }


    public String getDescription() {
        return description;
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
        return 0;
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
