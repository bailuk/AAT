package ch.bailu.aat_gtk.service.location.interfaces;

import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.annotations.DBusProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.messages.DBusSignal;
import org.freedesktop.dbus.types.UInt32;

/**
 * Semi-auto-generated class.
 */
@DBusInterfaceName("org.freedesktop.GeoClue2.Client")
@DBusProperty(name = "Location", type = DBusPath.class, access = Access.READ)
@DBusProperty(name = "DistanceThreshold", type = UInt32.class, access = Access.READ_WRITE)
@DBusProperty(name = "TimeThreshold", type = UInt32.class, access = Access.READ_WRITE)
@DBusProperty(name = "DesktopId", type = String.class, access = Access.READ_WRITE)
@DBusProperty(name = "RequestedAccuracyLevel", type = UInt32.class, access = Access.READ_WRITE)
@DBusProperty(name = "Active", type = Boolean.class, access = Access.READ)
public interface Client extends DBusInterface {


    void Start();
    void Stop();



    class LocationUpdated extends DBusSignal {

        private final DBusPath _old;
        private final DBusPath _new;

        public LocationUpdated(String _path, DBusPath _old, DBusPath _new) throws DBusException {
            super(_path);
            this._old = _old;
            this._new = _new;
        }

        public DBusPath getOld() {
            return _old;
        }
        public DBusPath getNew() {
            return _new;
        }
    }
}
