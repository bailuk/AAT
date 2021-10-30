package ch.bailu.aat_gtk.service.location

import ch.bailu.aat_gtk.service.location.interfaces.Client
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.service.location.*


/**
 * GeoClue2 DBus interface:
 * https://www.freedesktop.org/software/geoclue/docs/
 *
 * Java-DBus library:
 * https://github.com/hypfvieh/dbus-java
 *
 * Nice GUI DBus debugger:
 * https://wiki.gnome.org/Apps/DFeet/
 *
 * How to create GeoClue2 java interfaces:
 * 1. Install 'geoclue-2.0' package
 * 2. Get introspection files (xml-format):
 * 'ls /usr/share/dbus-1/interfaces/ * | grep GeoClue2'
 * 3. Clone 'https://github.com/hypfvieh/dbus-java' and read 'docs/code-generation.html'
 * 4. Generate java classes from introspection files according to documentation
 * 5. Manually adjust java files
 *
 */
class GeoClue2LocationProvider(
    serviceInterface: LocationServiceInterface,
    item: LocationStackItem?
) :
    LocationStackChainedItem(item) {
    private var geoClue2: GeoClue2Dbus? = null
    private val lock: Any
    private fun updateStateAndLocation(signal: Client.LocationUpdated) {
        synchronized(lock) {
            try {
                val geoClue2 = geoClue2
                if (geoClue2 != null) {
                    val location: LocationInformation = geoClue2.getLocation(signal.new)
                    passLocation(location)
                } else {
                    passState(StateID.NOSERVICE)
                }

            } catch (e: Exception) {
                passState(StateID.NOSERVICE)
            }
        }
    }

    private fun updateState() {
        synchronized(lock) {
            val geoClue2 = geoClue2
            if (geoClue2 != null && geoClue2.active) {
                passState(LocationService.INITIAL_STATE)
            } else {
                passState(StateID.OFF)
            }
        }
    }

    override fun close() {
        geoClue2?.stop()
    }

    init {
        lock = serviceInterface
        object :
            Thread(GeoClue2Dbus::class.java.simpleName) {
            override fun run() {
                try {
                    val geo = GeoClue2Dbus()
                    geo.connect { signal: Client.LocationUpdated ->
                        updateStateAndLocation(
                            signal
                        )
                    }
                    geo.start()
                    geoClue2 = geo
                    updateState()
                } catch (e: Exception) {
                    passState(StateID.NOSERVICE)
                }
            }
        }.start()
    }
}
