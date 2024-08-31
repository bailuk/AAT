package ch.bailu.aat_gtk.service.location

import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.gpx.information.StateID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.service.location.LocationStackChainedItem
import ch.bailu.aat_lib.service.location.LocationStackItem
import ch.bailu.gtk.geoclue.AccuracyLevel
import ch.bailu.gtk.geoclue.ClientProxy
import ch.bailu.gtk.geoclue.Location
import ch.bailu.gtk.geoclue.Simple
import ch.bailu.gtk.gio.AsyncResult
import ch.bailu.gtk.type.Str


class GeoClue2LocationProvider(item: LocationStackItem) : LocationStackChainedItem(item) {

    private var client: ClientProxy? = null

    private fun updateStateAndLocation(location: Location) {
        passLocation(GeoClue2LocationInformation(location, StateID.ON))
    }

    override fun close() {
        val client = this.client
        if (client is ClientProxy) {
            client.disconnectSignals()
            client.unref()
            this.client = null
        }
    }

    init {
        try {
            val appId = AppConfig.getInstance().appId
            Simple.newWithThresholds(Str(appId), AccuracyLevel.EXACT, 0, 0, null,
                { self, _ , res: AsyncResult, _ ->
                    try {
                        val simple = Simple.newWithThresholdsFinishSimple(res)
                        val client = simple.client
                        client.ref()
                        AppLog.d(this, "Client object: " + client.objectPath)
                        client.onNotify { updateStateAndLocation(simple.location) }
                        this.client = client
                        updateStateAndLocation(simple.location)
                    } catch (e: java.lang.Exception) {
                        passState(StateID.NO_SERVICE)
                        AppLog.e(this, "Failed to connect to GeoClue2 service")
                    }
                    self.unregister()
                }, null
            )
        } catch (e: java.lang.Exception) {
            passState(StateID.NO_SERVICE)
            AppLog.e(this, "Failed to initialize GeoClue2")
        }
    }
}
