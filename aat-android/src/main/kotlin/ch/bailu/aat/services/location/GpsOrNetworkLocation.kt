package ch.bailu.aat.services.location

import android.content.Context
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.service.location.LocationInformation
import ch.bailu.aat_lib.service.location.LocationStackChainedItem
import ch.bailu.aat_lib.service.location.LocationStackItem

class GpsOrNetworkLocation(next: LocationStackItem, context: Context, interval: Int) :
    LocationStackChainedItem(next) {
    private val network: NetworkLocation
    private val gps: GpsLocation
    private var haveGps = false

    init {
        network = NetworkLocation(object : LocationStackItem() {
            override fun passState(state: Int) {}
            override fun passLocation(location: LocationInformation) {
                if (!haveGps) this@GpsOrNetworkLocation.passLocation(location)
            }
        }, context, interval * 5)

        gps = GpsLocation(object : LocationStackItem() {
            override fun passState(state: Int) {
                haveGps = state == StateID.ON
                this@GpsOrNetworkLocation.passState(state)
            }

            override fun passLocation(location: LocationInformation) {
                this@GpsOrNetworkLocation.passLocation(location)
            }
        }, context, interval)
    }

    override fun close() {
        super.close()
        network.close()
        gps.close()
    }

    override fun appendStatusText(builder: StringBuilder) {
        super.appendStatusText(builder)
        network.appendStatusText(builder)
        gps.appendStatusText(builder)
        if (haveGps) {
            builder.append("have GPS")
        }
    }
}
