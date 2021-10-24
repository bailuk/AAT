package ch.bailu.aat_gtk.service.location.interfaces;

import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.annotations.DBusProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.interfaces.DBusInterface;


/**
 * Semi-auto-generated class.
 */
@DBusInterfaceName("org.freedesktop.GeoClue2.Location")
@DBusProperty(name = "Latitude", type = Double.class, access = Access.READ)
@DBusProperty(name = "Longitude", type = Double.class, access = Access.READ)
@DBusProperty(name = "Accuracy", type = Double.class, access = Access.READ)
@DBusProperty(name = "Altitude", type = Double.class, access = Access.READ)
@DBusProperty(name = "Speed", type = Double.class, access = Access.READ)
@DBusProperty(name = "Heading", type = Double.class, access = Access.READ)
@DBusProperty(name = "Description", type = String.class, access = Access.READ)
@DBusProperty(name = "Timestamp", type = PropertyTimestampStruct.class, access = DBusProperty.Access.READ)
public interface Location extends DBusInterface {



}

