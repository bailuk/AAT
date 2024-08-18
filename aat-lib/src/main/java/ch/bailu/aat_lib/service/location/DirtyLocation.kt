package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.coordinates.LatLongInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.SolidMapPosition.readPosition
import ch.bailu.aat_lib.preferences.location.SolidMapPosition.writePosition
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName

class DirtyLocation(next: LocationStackItem, private val storage: StorageInterface,
                    private val broadcast: Broadcaster
) : LocationStackChainedItem(next) {
    var locationInformation: GpxInformation
        private set
    private var state = LocationService.INITIAL_STATE


    init {
        locationInformation = OldLocation(storage)
    }


    override fun close() {
        writePosition(storage, SOLID_KEY, locationInformation)
    }


    override fun passLocation(location: LocationInformation) {
        locationInformation = location
        super.passLocation(location)
        broadcast.broadcast(AppBroadcaster.LOCATION_CHANGED)
    }

    override fun passState(state: Int) {
        super.passState(state)
        this.state = state
        broadcast.broadcast(AppBroadcaster.LOCATION_CHANGED)
    }


    internal inner class OldLocation(storage: StorageInterface) : GpxInformation() {
        private var longitude = 0
        private var latitude = 0

        private val file: Foc = FocName(OldLocation::class.java.simpleName)

        init {
            readPosition(storage)
        }

        override fun getFile(): Foc {
            return file
        }


        private fun readPosition(storage: StorageInterface) {
            val latLongE6: LatLongInterface = readPosition(storage, SOLID_KEY)

            longitude = latLongE6.getLongitudeE6()
            latitude = latLongE6.getLatitudeE6()
        }

        override fun getLongitudeE6(): Int {
            return longitude
        }

        override fun getLatitudeE6(): Int {
            return latitude
        }

        override fun getLongitude(): Double {
            return (longitude.toDouble()) / 1e6
        }

        override fun getLatitude(): Double {
            return (latitude.toDouble()) / 1e6
        }

        override fun getState(): Int {
            return this@DirtyLocation.state
        }
    }

    companion object {
        private const val SOLID_KEY = "DirtyLocation_"
    }
}
