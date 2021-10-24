package ch.bailu.aat_gtk.service

import ch.bailu.aat_gtk.solid.GtkSolidLocationProvider
import ch.bailu.aat_gtk.solid.SolidGtkDataDirectory
import ch.bailu.aat_gtk.view.GtkStatusIcon
import ch.bailu.aat_lib.dispatcher.Broadcaster
import ch.bailu.aat_lib.factory.FocFactory
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.IconMapServiceInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.cache.ObjeImageInterface
import ch.bailu.aat_lib.service.elevation.ElevetionServiceInterface
import ch.bailu.aat_lib.service.location.LocationService
import ch.bailu.aat_lib.service.location.LocationServiceInterface
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface
import ch.bailu.aat_lib.service.tracker.TrackerService
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface
import ch.bailu.foc.FocFile

class GtkServices (storage: StorageInterface, broadcaster: Broadcaster) :
    ServicesInterface {
    private val locationService: LocationService
    private val trackerService: TrackerService
    override fun getLocationService(): LocationServiceInterface {
        return locationService
    }

    override fun lock(simpleName: String) {}
    override fun free(simpleName: String) {}
    override fun lock(): Boolean {
        return true
    }

    override fun free() {}
    override fun getElevationService(): ElevetionServiceInterface {
        return ElevetionServiceInterface { latitudeE6, longitudeE6 -> 0 }
    }

    override fun getSensorService(): SensorServiceInterface {
        return object : SensorServiceInterface {
            override fun getInformationOrNull(infoID: Int): GpxInformation? {
                return null
            }

            override fun getInformation(iid: Int): GpxInformation? {
                return null
            }

            override fun updateConnections() {}
            override fun scan() {}
        }
    }

    override fun getTrackerService(): TrackerServiceInterface {
        return trackerService
    }

    override fun getIconMapService(): IconMapServiceInterface {
        return object : IconMapServiceInterface {
            override fun getIconSVG(point: GpxPointInterface, icon_size: Int): ObjeImageInterface? {
                return null
            }

            override fun toAssetPath(key: Int, value: String): String {
                return ""
            }

            override fun toAssetPath(gpxPointNode: GpxPointNode): String {
                return ""
            }
        }
    }

    init {
        val factory = FocFactory { string: String? -> FocFile(string) }
        locationService = LocationService(GtkSolidLocationProvider(storage), broadcaster)
        trackerService = TrackerService(
            SolidGtkDataDirectory(storage, factory),
            GtkStatusIcon(),
            broadcaster,
            this
        )
    }
}
