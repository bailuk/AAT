package ch.bailu.aat_gtk.service.location.interfaces;

import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.annotations.DBusProperty;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;

/**
 * Auto-generated class.
 */
@DBusInterfaceName("org.freedesktop.GeoClue2.Manager")
@DBusProperty(name = "InUse", type = Boolean.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "AvailableAccuracyLevel", type = UInt32.class, access = DBusProperty.Access.READ)
public interface Manager extends DBusInterface {
    DBusPath GetClient();
    DBusPath CreateClient();
    void DeleteClient(DBusPath client);
    void AddAgent(String id);
}
