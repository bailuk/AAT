package ch.bailu.aat_awt.services.location;

import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusSigHandler;
import org.freedesktop.dbus.interfaces.Properties;
import org.freedesktop.dbus.types.UInt32;

import ch.bailu.aat_awt.services.location.interfaces.Client;
import ch.bailu.aat_awt.services.location.interfaces.Location;
import ch.bailu.aat_awt.services.location.interfaces.Manager;

public class GeoClue2Dbus {
    public final static String DESKTOP_ID = "ch.bailu.aat_awt";
    public final static String BUS_NAME = "org.freedesktop.GeoClue2";
    public final static String MANAGER_PATH = "/org/freedesktop/GeoClue2/Manager";
    public final static String CLIENT_INTERFACE = "org.freedesktop.GeoClue2.Client";

    private final DBusConnection connection;

    private final String client_path;

    private final Manager manager;
    private final Client client;
    private final Properties properties;



    public GeoClue2Dbus() throws DBusException {
        connection = DBusConnection.getConnection(DBusConnection.DBusBusType.SYSTEM);

        manager = connection.getRemoteObject(
                BUS_NAME,
                MANAGER_PATH,
                Manager.class);

        client_path = manager.GetClient().getPath();

        client = connection.getRemoteObject(BUS_NAME, client_path,Client.class);
        properties = connection.getRemoteObject(BUS_NAME, client_path, Properties.class);

        setDesktopId(DESKTOP_ID);
    }


    public void start() {
        client.Start();
    }
    public void stop() {
        client.Stop();
    }

    public void connect(DBusSigHandler<Client.LocationUpdated> handler) throws DBusException {
        connection.addSigHandler(Client.LocationUpdated.class, client, handler);
    }

    public String getDesktopId() {
        return properties.Get(CLIENT_INTERFACE, "DesktopId");
    }

    public void setDesktopId(String id) {
        properties.Set(CLIENT_INTERFACE, "DesktopId", id);
    }

    public LocationReader getLocation() throws DBusException {
        return getLocation(properties.Get(CLIENT_INTERFACE,"Location"));
    }

    public LocationReader getLocation(DBusPath path) throws DBusException {
        return new LocationReader(connection, connection.getRemoteObject(BUS_NAME, path.getPath(), Location.class));
    }

    public UInt32 getDistanceThreshold() {
        return properties.Get(CLIENT_INTERFACE,"DistanceThreshold");
    }

    public void setDistanceThreshold(UInt32 value) {
        properties.Set(CLIENT_INTERFACE,"DistanceThreshold", value);
    }

    public UInt32 getTimeThreshold() {
        return properties.Get(CLIENT_INTERFACE,"TimeThreshold");
    }

    public void setTimeThreshold(UInt32 value) {
        properties.Set(CLIENT_INTERFACE,"TimeThreshold", value);
    }

    public UInt32 getRequestedAccuracyLevel() {
        return properties.Get(CLIENT_INTERFACE,"RequestedAccuracyLevel");
    }

    public void setRequestedAccuracyLevel(UInt32 level) {
        properties.Set(CLIENT_INTERFACE,"RequestedAccuracyLevel", level);
    }

    public Boolean getActive() {
        return properties.Get(CLIENT_INTERFACE,"Active");
    }
}
