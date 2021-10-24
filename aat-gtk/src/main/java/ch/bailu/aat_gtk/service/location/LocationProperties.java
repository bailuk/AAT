package ch.bailu.aat_gtk.service.location;

import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.interfaces.Properties;
import org.freedesktop.dbus.types.UInt64;

public class LocationProperties {
    private final static String LOCATION_INTERFACE = "org.freedesktop.GeoClue2.Location";

    private final Properties properties;

    public LocationProperties(DBusConnection connection, DBusInterface location) throws DBusException {
        properties = connection.getRemoteObject(GeoClue2Dbus.BUS_NAME, location.getObjectPath(), Properties.class);

    }


    public Double readDouble(String key) {
        return properties.Get(LOCATION_INTERFACE, key);
    }

    public String readDescription() {
        return properties.Get(LOCATION_INTERFACE, "Description");
    }

    public long readTimeStamp() {
        Object times[];

        times = properties.Get(LOCATION_INTERFACE, "Timestamp");

        return ((UInt64) times[0]).longValue()*1000;
    }
}
